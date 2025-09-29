using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp5
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }
        // --- Validation helpers ---
        private bool ValidateUser()
        {
            if (string.IsNullOrWhiteSpace(txtUser.Text))
            {
                errorProvider1.SetError(txtUser, "Vui lòng nhập username");
                return false;
            }
            errorProvider1.SetError(txtUser, "");
            return true;
        }

        private bool ValidatePass()
        {
            if (string.IsNullOrWhiteSpace(txtPass.Text))
            {
                errorProvider1.SetError(txtPass, "Vui lòng nhập password");
                return false;
            }
            errorProvider1.SetError(txtPass, "");
            return true;
        }

        // Sự kiện Leave: cảnh báo ngay khi rời ô
        private void txtUser_Leave(object sender, EventArgs e) => ValidateUser();
        private void txtPass_Leave(object sender, EventArgs e) => ValidatePass();

        private void chkShow_CheckedChanged(object sender, EventArgs e)
        {
            txtPass.UseSystemPasswordChar = !chkShow.Checked;
        }

        private void btnLogin_Click(object sender, EventArgs e)
        {
            // Kiểm tra không để trống
            bool ok = ValidateUser() & ValidatePass(); // dùng & để chạy cả hai hàm
            if (!ok)
            {
                // đưa focus tới ô đầu tiên còn lỗi
                if (!string.IsNullOrEmpty(errorProvider1.GetError(txtUser))) txtUser.Focus();
                else txtPass.Focus();
                return;
            }

            // (Không yêu cầu xác thực tài khoản thực; nếu cần, bạn có thể check cứng ở đây)
            // Ví dụ: if (txtUser.Text=="admin" && txtPass.Text=="123") ...

            MessageBox.Show("Đăng nhập thành công!", "Thông báo",
                            MessageBoxButtons.OK, MessageBoxIcon.Information);

            // TODO: mở form chính của ứng dụng ở đây (nếu có)
            // this.Hide(); new MainForm().ShowDialog(); this.Close();
        }

        private void btnExit_Click(object sender, EventArgs e) => Close();
    }
}
