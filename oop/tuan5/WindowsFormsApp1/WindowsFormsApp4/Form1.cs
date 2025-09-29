using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp4
{
    public partial class Form1 : Form
    {
        // Giá dịch vụ (VND)
        private const int GiaCaoRang = 50_000;      // /2 hàm (trọn gói)
        private const int GiaTayTrang = 100_000;    // /2 hàm (trọn gói)
        private const int GiaHanRang = 100_000;     // /1 răng
        private const int GiaBeRang = 10_000;      // /1 răng
        private const int GiaBocRang = 1_000_000;   // /1 răng

        private readonly CultureInfo viVN = new CultureInfo("vi-VN");

        public Form1()
        {
            InitializeComponent();
        }

        // Bật/tắt NumericUpDown theo checkbox
        private void chkHanRang_CheckedChanged(object sender, EventArgs e)
        {
            nudHan.Enabled = chkHanRang.Checked;
        }
        private void chkBeRang_CheckedChanged(object sender, EventArgs e)
        {
            nudBe.Enabled = chkBeRang.Checked;
        }
        private void chkBocRang_CheckedChanged(object sender, EventArgs e)
        {
            nudBoc.Enabled = chkBocRang.Checked;
        }

        // Kiểm tra tên khách không rỗng
        private void txtTen_Leave(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtTen.Text))
                errorProvider1.SetError(txtTen, "Tên khách hàng không được để trống");
            else
                errorProvider1.SetError(txtTen, "");
        }

        private void btnTinhTien_Click(object sender, EventArgs e)
        {
            // Validate tên
            if (string.IsNullOrWhiteSpace(txtTen.Text))
            {
                errorProvider1.SetError(txtTen, "Tên khách hàng không được để trống");
                txtTen.Focus();
                return;
            }
            errorProvider1.SetError(txtTen, "");

            long tong = 0;

            if (chkCaoRang.Checked) tong += GiaCaoRang;
            if (chkTayTrang.Checked) tong += GiaTayTrang;

            if (chkHanRang.Checked) tong += GiaHanRang * (int)nudHan.Value;
            if (chkBeRang.Checked) tong += GiaBeRang * (int)nudBe.Value;
            if (chkBocRang.Checked) tong += GiaBocRang * (int)nudBoc.Value;

            txtThanhTien.Text = string.Format(viVN, "{0:c0}", tong);
            MessageBox.Show(
                $"Khách hàng: {txtTen.Text}\nTổng tiền: {txtThanhTien.Text}",
                "Hóa đơn thanh toán",
                MessageBoxButtons.OK, MessageBoxIcon.Information
            );
        }

        private void btnThoat_Click(object sender, EventArgs e)
        {
            Close();
        }
    }
}
