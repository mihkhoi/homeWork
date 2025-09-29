using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Media;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp3
{
    public partial class Form1 : Form
    {
        private readonly Dictionary<string, string> _passToGroup = new Dictionary<string, string>
        {
            // Phát triển công nghệ
            ["1496"] = "Phát triển công nghệ",
            ["2673"] = "Phát triển công nghệ",

            // Nghiên cứu viên
            ["7462"] = "Nghiên cứu viên",

            // Thiết kế mô hình
            ["8884"] = "Thiết kế mô hình",
            ["3842"] = "Thiết kế mô hình",
            ["3383"] = "Thiết kế mô hình",
        };
        public Form1()
        {
            InitializeComponent();
        }
        private void Digit_Click(object sender, EventArgs e)
        {
            if (sender is Button b && b.Tag is string d)
            {
                // Giới hạn độ dài 4 ký tự (password 4 số)
                if (txtPassword.TextLength < 4)
                    txtPassword.Text += d;
            }
        }

        // Xóa
        private void btnClear_Click(object sender, EventArgs e)
        {
            txtPassword.Clear();
            txtPassword.Focus();
        }

        // Enter
        private void btnEnter_Click(object sender, EventArgs e)
        {
            var code = txtPassword.Text.Trim();
            if (code.Length == 0)
            {
                SystemSounds.Beep.Play();
                return;
            }

            if (_passToGroup.TryGetValue(code, out string group))
            {
                Log(group, "Chấp nhận!");
            }
            else
            {
                Log("Không có", "Từ chối!");
            }

            txtPassword.Clear();
            txtPassword.Focus();
        }

        // Ring – báo động nho nhỏ
        private void btnRing_Click(object sender, EventArgs e)
        {
            SystemSounds.Hand.Play();
            MessageBox.Show("Chuông reo!", "RING", MessageBoxButtons.OK, MessageBoxIcon.Exclamation);
        }

        // Ghi log xuống DataGridView
        private void Log(string group, string result)
        {
            dgvLog.Rows.Insert(0,
                DateTime.Now.ToString("g"), // short date + short time theo culture
                group,
                result);
        }
    }
}
