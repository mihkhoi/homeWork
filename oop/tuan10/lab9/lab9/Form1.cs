using Microsoft.Reporting.WinForms;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace lab9
{
    public partial class Form1 : Form
    {
        // Sửa chuỗi kết nối cho đúng máy bạn
        private readonly string strCon =
            @"Data Source=(localdb)\MSSQLLocalDB;Initial Catalog=QuanLySinhVien;Integrated Security=True;TrustServerCertificate=True";

        public Form1()
        {
            InitializeComponent();
            this.Load += Form1_Load;
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            try
            {
                // Lấy dữ liệu
                DataTable dt = LaySinhVien();

                // RDLC ở ROOT project → copy ra bin, nên đường dẫn là baseDir\rptSinhVien.rdlc
                string reportPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "rptSinhVien.rdlc");

                if (!File.Exists(reportPath))
                {
                    // Gợi ý khắc phục nếu quên Copy Always
                    throw new FileNotFoundException(
                        "Không tìm thấy RDLC. Hãy chọn file rptSinhVien.rdlc → Build Action=Content, Copy to Output=Copy always.",
                        reportPath
                    );
                }

                reportViewer1.ProcessingMode = ProcessingMode.Local;
                reportViewer1.LocalReport.ReportPath = reportPath;

                reportViewer1.LocalReport.DataSources.Clear();
                // Tên "ds1" phải trùng Dataset trong RDLC
                reportViewer1.LocalReport.DataSources.Add(new ReportDataSource("ds1", dt));

                reportViewer1.RefreshReport();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Lỗi báo cáo: " + ex.Message, "Report", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private DataTable LaySinhVien()
        {
            using (var con = new SqlConnection(strCon))
            using (var da = new SqlDataAdapter(
                "SELECT MaSV, TenSV, GioiTinh, NgaySinh, QueQuan, MaLop FROM dbo.SinhVien", con))
            {
                var dt = new DataTable("SinhVien");
                da.Fill(dt);
                return dt;
            }
        }
    }
}
