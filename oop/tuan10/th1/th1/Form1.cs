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

namespace th1
{
    public partial class Form1 : Form
    {
        // ===== Chuỗi kết nối: CHỌN A hoặc B như mục (3) ở trên =====
        private readonly string strCon = @"Data Source=(localdb)\MSSQLLocalDB;
Initial Catalog=QuanLyBanSach;
Integrated Security=True;TrustServerCertificate=True;Connect Timeout=30";

        private SqlConnection sqlCon;  // có thể để null, mở khi cần

        public Form1()
        {
            InitializeComponent();

            // Khuyến nghị một vài thuộc tính DataGridView
            dgvDanhSach.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dgvDanhSach.ReadOnly = true;
            dgvDanhSach.AllowUserToAddRows = false;

            btnHienThi.Click += btnHienThi_Click;
        }

        private void MoKetNoi()
        {
            if (sqlCon == null)
                sqlCon = new SqlConnection(strCon);

            if (sqlCon.State == ConnectionState.Closed)
                sqlCon.Open();
        }

        private void DongKetNoi()
        {
            if (sqlCon != null && sqlCon.State == ConnectionState.Open)
                sqlCon.Close();
        }

        private void TaiDanhSach()
        {
            // DÙNG using để tự giải phóng adapter/command
            string sql = "SELECT MaNXB, TenNXB, DiaChi, DienThoai FROM dbo.NhaXuatBan";

            using (var cmd = new SqlCommand(sql, sqlCon))
            using (var adapter = new SqlDataAdapter(cmd))
            {
                var ds = new DataSet();
                adapter.Fill(ds, "NhaXuatBan");
                dgvDanhSach.DataSource = ds.Tables["NhaXuatBan"];
            }
        }

        private void btnHienThi_Click(object sender, EventArgs e)
        {
            try
            {
                MoKetNoi();
                TaiDanhSach();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Lỗi khi tải dữ liệu: " + ex.Message,
                    "Thông báo", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            finally
            {
                DongKetNoi();
            }
        }
    }
}
