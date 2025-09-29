using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp6
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
            UpdateButtons();
        }
        // ——— Nhập chỉ số nguyên ———
        private void txtSo_KeyPress(object sender, KeyPressEventArgs e)
        {
            // Cho phép: số, phím điều khiển (Backspace), dấu trừ ở đầu
            if (char.IsControl(e.KeyChar)) return;

            if (char.IsDigit(e.KeyChar))
            {
                // ok
            }
            else if (e.KeyChar == '-' && txtSo.SelectionStart == 0 && !txtSo.Text.Contains("-"))
            {
                // cho phép dấu âm ở đầu
            }
            else
            {
                e.Handled = true; // chặn ký tự khác
            }
        }

        // Enter = nhập số
        private void txtSo_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter)
            {
                btnNhap.PerformClick();
                e.SuppressKeyPress = true; // tránh beep
            }
        }

        // Thêm số vào ListBox
        private void btnNhap_Click(object sender, EventArgs e)
        {
            if (!int.TryParse(txtSo.Text, out int n))
            {
                MessageBox.Show("Vui lòng nhập số nguyên hợp lệ!", "Thông báo",
                                MessageBoxButtons.OK, MessageBoxIcon.Information);
                txtSo.Focus(); txtSo.SelectAll();
                return;
            }

            lsbDaySo.Items.Add(n);
            txtSo.Clear();
            txtSo.Focus();
            UpdateButtons();
        }

        // ——— Các chức năng ———
        private void btnTang2_Click(object sender, EventArgs e)
        {
            for (int i = 0; i < lsbDaySo.Items.Count; i++)
            {
                int v = Convert.ToInt32(lsbDaySo.Items[i]);
                lsbDaySo.Items[i] = v + 2;
            }
        }

        private void btnChonChanDau_Click(object sender, EventArgs e)
        {
            for (int i = 0; i < lsbDaySo.Items.Count; i++)
            {
                if (Convert.ToInt32(lsbDaySo.Items[i]) % 2 == 0)
                {
                    lsbDaySo.SelectedIndex = i;
                    return;
                }
            }
            MessageBox.Show("Không có số chẵn nào trong dãy.", "Thông báo");
        }

        private void btnChonLeCuoi_Click(object sender, EventArgs e)
        {
            for (int i = lsbDaySo.Items.Count - 1; i >= 0; i--)
            {
                if (Math.Abs(Convert.ToInt32(lsbDaySo.Items[i])) % 2 == 1)
                {
                    lsbDaySo.SelectedIndex = i;
                    return;
                }
            }
            MessageBox.Show("Không có số lẻ nào trong dãy.", "Thông báo");
        }

        private void btnXoaDangChon_Click(object sender, EventArgs e)
        {
            int idx = lsbDaySo.SelectedIndex;
            if (idx >= 0) lsbDaySo.Items.RemoveAt(idx);
            UpdateButtons();
        }

        private void btnXoaDau_Click(object sender, EventArgs e)
        {
            if (lsbDaySo.Items.Count > 0)
                lsbDaySo.Items.RemoveAt(0);
            UpdateButtons();
        }

        private void btnXoaCuoi_Click(object sender, EventArgs e)
        {
            int c = lsbDaySo.Items.Count;
            if (c > 0) lsbDaySo.Items.RemoveAt(c - 1);
            UpdateButtons();
        }

        private void btnXoaDaySo_Click(object sender, EventArgs e)
        {
            lsbDaySo.Items.Clear();
            UpdateButtons();
        }

        private void btnKetThuc_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void lsbDaySo_SelectedIndexChanged(object sender, EventArgs e)
        {
            UpdateButtons();
        }

        private void UpdateButtons()
        {
            bool hasItems = lsbDaySo.Items.Count > 0;
            btnTang2.Enabled = hasItems;
            btnChonChanDau.Enabled = hasItems;
            btnChonLeCuoi.Enabled = hasItems;
            btnXoaDau.Enabled = hasItems;
            btnXoaCuoi.Enabled = hasItems;
            btnXoaDaySo.Enabled = hasItems;
            btnXoaDangChon.Enabled = lsbDaySo.SelectedIndex >= 0;
        }
    }
}
