using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp2
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }
        // USCLN (GCD)
        private int GCD(int a, int b)
        {
            a = Math.Abs(a); b = Math.Abs(b);
            while (b != 0)
            {
                int t = b;
                b = a % b;
                a = t;
            }
            return a;
        }

        // BSCNN (LCM)
        private int LCM(int a, int b)
        {
            a = Math.Abs(a); b = Math.Abs(b);
            if (a == 0 || b == 0) return 0;
            return a / GCD(a, b) * b;
        }

        // === Sửa lỗi: thêm đúng 2 handler mà Designer đang gọi ===
        private void btnTim_Click(object sender, EventArgs e)
        {
            if (!int.TryParse(txtA.Text, out int a) || !int.TryParse(txtB.Text, out int b))
            {
                MessageBox.Show("Vui lòng nhập số nguyên hợp lệ!", "Lỗi",
                    MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            if (radUSCLN.Checked)
                txtKQ.Text = GCD(a, b).ToString();
            else if (radBSCNN.Checked)
                txtKQ.Text = LCM(a, b).ToString();
            else
                MessageBox.Show("Hãy chọn USCLN hoặc BSCNN.");
        }

        private void btnThoat_Click(object sender, EventArgs e)
        {
            Close();
        }
    }
}
