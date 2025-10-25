using System.Security.Cryptography;
using System.Text;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.Data.SqlClient;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using QuizApi.Data;
using QuizApi.Models;
using QuizApi.Services;

namespace QuizApi;

public class Program
{
    public static void Main(string[] args)
    {
        var builder = WebApplication.CreateBuilder(args);

        // DbContext
        builder.Services.AddDbContext<AppDb>(options =>
            options.UseSqlServer(builder.Configuration.GetConnectionString("SqlServer")));

        // JWT
        builder.Services.AddScoped<JwtService>();
        builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
            .AddJwtBearer(options =>
            {
                options.TokenValidationParameters = new TokenValidationParameters
                {
                    ValidateIssuer = true,
                    ValidateAudience = true,
                    ValidateIssuerSigningKey = true,
                    ValidIssuer = builder.Configuration["Jwt:Issuer"],
                    ValidAudience = builder.Configuration["Jwt:Audience"],
                    IssuerSigningKey = new SymmetricSecurityKey(
                        Encoding.UTF8.GetBytes(builder.Configuration["Jwt:Key"]!))
                };
            });

        builder.Services.AddAuthorization();
        builder.Services.AddEndpointsApiExplorer();
        builder.Services.AddSwaggerGen();
        builder.Services.AddCors(p => p.AddDefaultPolicy(
            b => b.AllowAnyOrigin().AllowAnyHeader().AllowAnyMethod()));

        var app = builder.Build();

        app.UseSwagger();
        app.UseSwaggerUI();
        app.UseCors();
        app.UseAuthentication();
        app.UseAuthorization();

        // LOGIN
        app.MapPost("/auth/login", async (LoginDto dto, IConfiguration cfg, JwtService jwt) =>
        {
            using var sha = SHA256.Create();
            var passHash = sha.ComputeHash(Encoding.UTF8.GetBytes(dto.Password));

            await using var conn = new SqlConnection(cfg.GetConnectionString("SqlServer"));
            await conn.OpenAsync();

            var cmd = new SqlCommand(@"
                SELECT TOP 1 u.Id, u.Username, u.FullName, r.Code RoleCode
                FROM Users u JOIN Roles r ON r.Id = u.RoleId
                WHERE u.Username = @u AND u.PasswordHash = @p AND u.IsActive = 1", conn);
            cmd.Parameters.AddWithValue("@u", dto.Username);
            cmd.Parameters.AddWithValue("@p", passHash);

            using var rd = await cmd.ExecuteReaderAsync();
            if (!rd.Read()) return Results.Unauthorized();

            var user = new User { Id = rd.GetInt32(0), Username = rd.GetString(1), FullName = rd.GetString(2) };
            var roleCode = rd.GetString(3);
            var token = jwt.CreateToken(user, roleCode);
            return Results.Ok(new { token, user = new { user.Id, user.Username, user.FullName, roleCode } });
        });

        // Lấy danh sách môn
        app.MapGet("/subjects", async (AppDb db) =>
            await db.Subjects.OrderBy(s => s.Name).ToListAsync());

        app.Run();
    }
}

// DTO
public record LoginDto(string Username, string Password);
