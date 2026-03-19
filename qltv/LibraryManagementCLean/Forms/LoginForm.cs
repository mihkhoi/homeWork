using System;
using System.Drawing;
using System.Windows.Forms;
using LibraryManagementCLean.Data;
using Microsoft.Data.SqlClient;

namespace LibraryManagementCLean.Forms {
public
class LoginForm : Form {
  private
    TextBox txtUsername;
  private
    TextBox txtPassword;
  private
    Label lblStatus;

  public
    string FullName {
        get;
      private
        set;
    }
    = "Quản lý";

  public
    LoginForm() {
        BuildUi();
        Load += LoginForm_Load;
    }

  private
    void BuildUi() {
        Text = "Đăng nhập - Quản lý Thư viện";
        StartPosition = FormStartPosition.CenterScreen;
        FormBorderStyle = FormBorderStyle.FixedDialog;
        MaximizeBox = false;
        MinimizeBox = false;
        ClientSize = new Size(540, 420);
        BackColor = Color.FromArgb(236, 239, 241);

        Panel panel = new Panel();
        panel.BackColor = Color.White;
        panel.Location = new Point(45, 28);
        panel.Size = new Size(440, 330);

        Label lblTitle = new Label();
        lblTitle.Text = "QUẢN LÝ THƯ VIỆN";
        lblTitle.Font = new Font("Segoe UI", 24F, FontStyle.Bold);
        lblTitle.ForeColor = Color.FromArgb(33, 136, 198);
        lblTitle.AutoSize = true;
        lblTitle.Location = new Point(28, 20);

        Label lblSub = new Label();
        lblSub.Text = "Hệ thống quản lý thư viện đa máy LAN";
        lblSub.Font = new Font("Segoe UI", 11F);
        lblSub.AutoSize = true;
        lblSub.Location = new Point(32, 72);

        Label lblUser = new Label();
        lblUser.Text = "Tên đăng nhập";
        lblUser.Font = new Font("Segoe UI", 11F);
        lblUser.AutoSize = true;
        lblUser.Location = new Point(32, 120);

        txtUsername = new TextBox();
        txtUsername.Font = new Font("Segoe UI", 12F);
        txtUsername.Location = new Point(32, 148);
        txtUsername.Size = new Size(300, 34);

        Label lblPass = new Label();
        lblPass.Text = "Mật khẩu";
        lblPass.Font = new Font("Segoe UI", 11F);
        lblPass.AutoSize = true;
        lblPass.Location = new Point(32, 195);

        txtPassword = new TextBox();
        txtPassword.Font = new Font("Segoe UI", 12F);
        txtPassword.Location = new Point(32, 223);
        txtPassword.Size = new Size(300, 34);
        txtPassword.UseSystemPasswordChar = true;
        txtPassword.PlaceholderText = "Nhập mật khẩu";

        lblStatus = new Label();
        lblStatus.Text = "Đang kiểm tra kết nối...";
        lblStatus.ForeColor = Color.Green;
        lblStatus.AutoSize = true;
        lblStatus.Location = new Point(32, 270);

        Button btnLogin = new Button();
        btnLogin.Text = "Đăng nhập";
        btnLogin.Size = new Size(140, 40);
        btnLogin.Location = new Point(32, 290);
        btnLogin.BackColor = Color.FromArgb(33, 136, 198);
        btnLogin.ForeColor = Color.White;
        btnLogin.FlatStyle = FlatStyle.Flat;
        btnLogin.FlatAppearance.BorderSize = 0;
        btnLogin.Click += BtnLogin_Click;

        Button btnExit = new Button();
        btnExit.Text = "Thoát";
        btnExit.Size = new Size(140, 40);
        btnExit.Location = new Point(190, 290);
        btnExit.BackColor = Color.FromArgb(149, 165, 166);
        btnExit.ForeColor = Color.White;
        btnExit.FlatStyle = FlatStyle.Flat;
        btnExit.FlatAppearance.BorderSize = 0;
        btnExit.Click += (s, e) => {
            DialogResult = DialogResult.Cancel;
            Close();
        };

        panel.Controls.Add(lblTitle);
        panel.Controls.Add(lblSub);
        panel.Controls.Add(lblUser);
        panel.Controls.Add(txtUsername);
        panel.Controls.Add(lblPass);
        panel.Controls.Add(txtPassword);
        panel.Controls.Add(lblStatus);
        panel.Controls.Add(btnLogin);
        panel.Controls.Add(btnExit);

        Controls.Add(panel);
        AcceptButton = btnLogin;
    }

  private
    void LoginForm_Load(object sender, EventArgs e) {
        string error;
        if (DatabaseHelper.TestConnection(out error))
            lblStatus.Text = "✓ Đã kết nối đến máy chủ";
        else
            lblStatus.Text = "✗ Chưa kết nối được CSDL: " + error;
    }

  private
    void BtnLogin_Click(object sender, EventArgs e) {
        string sql = @" SELECT TOP 1 FullName
            FROM Users
                WHERE Username = @Username AND Password = @Password AND IsActive = 1 ";

            object result = DatabaseHelper.ExecuteScalar(
                sql,
                new SqlParameter("@Username", txtUsername.Text.Trim()),
                new SqlParameter("@Password", txtPassword.Text.Trim()));

        if (result != null && result != DBNull.Value) {
            FullName = result.ToString();
            DialogResult = DialogResult.OK;
            Close();
        } else {
            MessageBox.Show("Sai tài khoản hoặc mật khẩu.");
            txtPassword.Clear();
            txtPassword.Focus();
        }
    }
}
} // namespace LibraryManagementCLean. Forms

