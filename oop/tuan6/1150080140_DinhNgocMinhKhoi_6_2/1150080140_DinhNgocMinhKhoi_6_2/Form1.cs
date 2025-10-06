using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace _1150080140_DinhNgocMinhKhoi_6_2
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
            // Gắn sự kiện (nếu chưa gắn trong Designer)
            this.Load += Form1_Load;
            btnThem.Click += btnThem_Click;
            btnSua.Click += btnSua_Click;
            btnXoa.Click += btnXoa_Click;
            btnThoat.Click += btnThoat_Click;
            lvSinhVien.SelectedIndexChanged += lvSinhVien_SelectedIndexChanged;
        }

        // Khởi tạo ListView: cột + thuộc tính
        private void Form1_Load(object sender, EventArgs e)
        {
            lvSinhVien.View = View.Details;
            lvSinhVien.FullRowSelect = true;
            lvSinhVien.GridLines = true;
            lvSinhVien.HideSelection = false;

            // Tạo cột nếu chưa có
            if (lvSinhVien.Columns.Count == 0)
            {
                lvSinhVien.Columns.Add("Họ tên", 180);
                lvSinhVien.Columns.Add("Ngày sinh", 100);
                lvSinhVien.Columns.Add("Lớp", 100);
                lvSinhVien.Columns.Add("Địa chỉ", 220);
            }

            // Ngày sinh mặc định
            dtpNgaySinh.MaxDate = DateTime.Today;
        }

        // ====== Nút Thêm ======
        private void btnThem_Click(object sender, EventArgs e)
        {
            if (!ValidateInputs()) return;

            string hoTen = txtHoTen.Text.Trim();
            string lop = txtLop.Text.Trim();
            string diaChi = txtDiaChi.Text.Trim();
            string ngaySinh = dtpNgaySinh.Value.ToString("dd/MM/yyyy");

            // Tạo 1 dòng mới
            ListViewItem item = new ListViewItem(hoTen);
            item.SubItems.Add(ngaySinh);
            item.SubItems.Add(lop);
            item.SubItems.Add(diaChi);

            lvSinhVien.Items.Add(item);
            ClearInputs();
        }

        // ====== Nút Xóa ======
        private void btnXoa_Click(object sender, EventArgs e)
        {
            if (lvSinhVien.SelectedItems.Count == 0)
            {
                MessageBox.Show("Vui lòng chọn 1 dòng trong danh sách để xóa.",
                    "Thiếu lựa chọn", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            if (MessageBox.Show("Bạn có chắc muốn xóa dòng đã chọn?", "Xác nhận",
                MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
            {
                lvSinhVien.Items.Remove(lvSinhVien.SelectedItems[0]);
                ClearInputs();
            }
        }

        // ====== Nút Sửa (Cập nhật dòng đang chọn) ======
        private void btnSua_Click(object sender, EventArgs e)
        {
            if (lvSinhVien.SelectedItems.Count == 0)
            {
                MessageBox.Show("Vui lòng chọn 1 dòng trong danh sách để sửa.",
                    "Thiếu lựa chọn", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            if (!ValidateInputs()) return;

            var item = lvSinhVien.SelectedItems[0];
            item.Text = txtHoTen.Text.Trim();                           // cột 1
            item.SubItems[1].Text = dtpNgaySinh.Value.ToString("dd/MM/yyyy"); // cột 2
            item.SubItems[2].Text = txtLop.Text.Trim();                 // cột 3
            item.SubItems[3].Text = txtDiaChi.Text.Trim();              // cột 4
        }

        // ====== Nút Thoát ======
        private void btnThoat_Click(object sender, EventArgs e)
        {
            Close();
        }

        // Khi chọn 1 dòng trên ListView → đổ dữ liệu lên các ô nhập
        private void lvSinhVien_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (lvSinhVien.SelectedItems.Count == 0) return;

            var item = lvSinhVien.SelectedItems[0];
            txtHoTen.Text = item.Text;
            DateTime ns;
            if (DateTime.TryParseExact(item.SubItems[1].Text, "dd/MM/yyyy",
                System.Globalization.CultureInfo.InvariantCulture,
                System.Globalization.DateTimeStyles.None, out ns))
            {
                dtpNgaySinh.Value = ns;
            }
            txtLop.Text = item.SubItems[2].Text;
            txtDiaChi.Text = item.SubItems[3].Text;
        }

        // ====== Helpers ======
        private bool ValidateInputs()
        {
            if (string.IsNullOrWhiteSpace(txtHoTen.Text))
            {
                MessageBox.Show("Họ tên không được rỗng.", "Thiếu dữ liệu",
                    MessageBoxButtons.OK, MessageBoxIcon.Warning);
                txtHoTen.Focus();
                return false;
            }
            return true;
        }

        private void ClearInputs()
        {
            txtHoTen.Clear();
            txtLop.Clear();
            txtDiaChi.Clear();
            dtpNgaySinh.Value = DateTime.Today;
            lvSinhVien.SelectedItems.Clear();
            txtHoTen.Focus();
        }
    }
}
