using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace th2
{
    public partial class Form1 : Form
    {
        // Chuỗi kết nối – dùng Catalog cho dễ
        private readonly string strCon = @"Data Source=(localdb)\MSSQLLocalDB;
Initial Catalog=QuanLyBanSach;
Integrated Security=True;TrustServerCertificate=True;Connect Timeout=30";

        private SqlConnection sqlCon;

        public Form1()
        {
            InitializeComponent();
            dgvDanhSach.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dgvDanhSach.ReadOnly = true;
            dgvDanhSach.AllowUserToAddRows = false;

            // có sẵn sự kiện btnHienThi_Click từ bài 1
        }

        private void MoKetNoi()
        {
            if (sqlCon == null) sqlCon = new SqlConnection(strCon);
            if (sqlCon.State == ConnectionState.Closed) sqlCon.Open();
        }
        private void DongKetNoi()
        {
            if (sqlCon != null && sqlCon.State == ConnectionState.Open) sqlCon.Close();
        }

        private void HienThiDuLieu()
        {
            MoKetNoi();
            string sql = "SELECT MaNXB, TenNXB, DiaChi, DienThoai FROM dbo.NhaXuatBan";
            using (var da = new SqlDataAdapter(sql, sqlCon))
            {
                var dt = new DataTable();
                da.Fill(dt);
                dgvDanhSach.DataSource = dt;
            }
            DongKetNoi();
        }

        private void btnHienThi_Click(object sender, EventArgs e)
        {
            try { HienThiDuLieu(); }
            catch (Exception ex) { MessageBox.Show("Lỗi tải dữ liệu: " + ex.Message); }
        }

        private void btnThem_Click(object sender, EventArgs e)
        {
            // Kiểm tra dữ liệu nhập
            if (string.IsNullOrWhiteSpace(txtTenXB.Text))
            {
                MessageBox.Show("Vui lòng nhập Tên nhà xuất bản");
                txtTenXB.Focus();
                return;
            }

            try
            {
                MoKetNoi();
                string insertSql = @"INSERT INTO dbo.NhaXuatBan(TenNXB, DiaChi, DienThoai)
                                     VALUES (@Ten, @DiaChi, @DienThoai)";

                using (var cmd = new SqlCommand(insertSql, sqlCon))
                {
                    cmd.Parameters.AddWithValue("@Ten", txtTenXB.Text.Trim());
                    cmd.Parameters.AddWithValue("@DiaChi", (object?)txtDiaChi.Text.Trim() ?? DBNull.Value);
                    cmd.Parameters.AddWithValue("@DienThoai", (object?)txtDienThoai.Text.Trim() ?? DBNull.Value);

                    int kq = cmd.ExecuteNonQuery();
                    if (kq > 0)
                    {
                        MessageBox.Show("Thêm dữ liệu thành công!");
                        HienThiDuLieu();
                        XoaForm();
                    }
                    else
                    {
                        MessageBox.Show("Thêm dữ liệu không thành công!");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Lỗi khi thêm: " + ex.Message);
            }
            finally
            {
                DongKetNoi();
            }
        }

        private void btnClear_Click(object sender, EventArgs e) => XoaForm();

        private void XoaForm()
        {
            txtTenXB.Text = "";
            txtDiaChi.Text = "";
            txtDienThoai.Text = "";
            txtTenXB.Focus();
        }
    }
}
