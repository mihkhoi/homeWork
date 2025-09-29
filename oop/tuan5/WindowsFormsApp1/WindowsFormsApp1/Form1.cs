using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp1
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }
        private void btnCong_Click(object sender, EventArgs e)
        {
            if (decimal.TryParse(txtA.Text, out decimal a) && decimal.TryParse(txtB.Text, out decimal b))
            {
                txtKQ.Text = (a + b).ToString();
            }
            else
            {
                MessageBox.Show("Vui lòng nhập số hợp lệ!");
            }
        }

        private void btnTru_Click(object sender, EventArgs e)
        {
            if (decimal.TryParse(txtA.Text, out decimal a) && decimal.TryParse(txtB.Text, out decimal b))
            {
                txtKQ.Text = (a - b).ToString();
            }
            else
            {
                MessageBox.Show("Vui lòng nhập số hợp lệ!");
            }
        }

        private void btnNhan_Click(object sender, EventArgs e)
        {
            if (decimal.TryParse(txtA.Text, out decimal a) && decimal.TryParse(txtB.Text, out decimal b))
            {
                txtKQ.Text = (a * b).ToString();
            }
            else
            {
                MessageBox.Show("Vui lòng nhập số hợp lệ!");
            }
        }

        private void btnChia_Click(object sender, EventArgs e)
        {
            if (decimal.TryParse(txtA.Text, out decimal a) && decimal.TryParse(txtB.Text, out decimal b))
            {
                if (b == 0)
                {
                    MessageBox.Show("Không thể chia cho 0!");
                }
                else
                {
                    txtKQ.Text = (a / b).ToString();
                }
            }
            else
            {
                MessageBox.Show("Vui lòng nhập số hợp lệ!");
            }
        }

        private void btnXoa_Click(object sender, EventArgs e)
        {
            txtA.Clear();
            txtB.Clear();
            txtKQ.Clear();
            txtA.Focus();
        }

        private void btnThoat_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}
