using System;
using System.Drawing;
using System.Windows.Forms;
using LibraryManagementCLean.Data;
using Microsoft.Data.SqlClient;

namespace LibraryManagementCLean.Forms
{
    public class ReportForm : Form
    {
        private Label lblBooks;
        private Label lblBorrowing;
        private Label lblOverdue;
        private Label lblMembers;
        private DataGridView dgvTopBooks;
        private DateTimePicker dtFrom;
        private DateTimePicker dtTo;

        public ReportForm()
        {
            BuildUi();
            Load += ReportForm_Load;
        }

        private void BuildUi()
        {
            BackColor = Color.FromArgb(236, 239, 241);

            Label lblTitle = new Label();
            lblTitle.Text = "THỐNG KÊ - BÁO CÁO";
            lblTitle.Font = new Font("Segoe UI", 22F, FontStyle.Bold);
            lblTitle.AutoSize = true;
            lblTitle.Location = new Point(20, 15);

            dtFrom = new DateTimePicker();
            dtFrom.Location = new Point(20, 62);
            dtFrom.Width = 140;
            dtFrom.Format = DateTimePickerFormat.Short;
            dtFrom.Value = DateTime.Today.AddMonths(-1);

            dtTo = new DateTimePicker();
            dtTo.Location = new Point(180, 62);
            dtTo.Width = 140;
            dtTo.Format = DateTimePickerFormat.Short;
            dtTo.Value = DateTime.Today;

            Button btnLoad = new Button();
            btnLoad.Text = "Tạo báo cáo";
            btnLoad.Location = new Point(340, 58);
            btnLoad.Size = new Size(120, 34);
            btnLoad.BackColor = Color.FromArgb(33, 136, 198);
            btnLoad.ForeColor = Color.White;
            btnLoad.FlatStyle = FlatStyle.Flat;
            btnLoad.FlatAppearance.BorderSize = 0;
            btnLoad.Click += BtnLoad_Click;

            Controls.Add(lblTitle);
            Controls.Add(dtFrom);
            Controls.Add(dtTo);
            Controls.Add(btnLoad);

            Controls.Add(CreateCard("Tổng sách", Color.FromArgb(52, 152, 219), out lblBooks, 20));
            Controls.Add(CreateCard("Đang mượn", Color.FromArgb(46, 204, 113), out lblBorrowing, 220));
            Controls.Add(CreateCard("Quá hạn", Color.FromArgb(231, 76, 60), out lblOverdue, 420));
            Controls.Add(CreateCard("Độc giả", Color.FromArgb(155, 89, 182), out lblMembers, 620));

            Label lblGrid = new Label();
            lblGrid.Text = "Sách được mượn nhiều";
            lblGrid.Font = new Font("Segoe UI", 16F, FontStyle.Bold);
            lblGrid.AutoSize = true;
            lblGrid.Location = new Point(20, 235);

            dgvTopBooks = new DataGridView();
            dgvTopBooks.Location = new Point(20, 275);
            dgvTopBooks.Size = new Size(900, 360);
            dgvTopBooks.ReadOnly = true;
            dgvTopBooks.AllowUserToAddRows = false;
            dgvTopBooks.AllowUserToDeleteRows = false;
            dgvTopBooks.BackgroundColor = Color.White;
            dgvTopBooks.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dgvTopBooks.RowHeadersVisible = false;

            Controls.Add(lblGrid);
            Controls.Add(dgvTopBooks);
        }

        private Panel CreateCard(string title, Color color, out Label lblValue, int x)
        {
            Panel panel = new Panel();
            panel.BackColor = color;
            panel.Location = new Point(x, 120);
            panel.Size = new Size(180, 90);

            Label lblTitleCard = new Label();
            lblTitleCard.Text = title;
            lblTitleCard.AutoSize = true;
            lblTitleCard.ForeColor = Color.White;
            lblTitleCard.Font = new Font("Segoe UI", 10F);
            lblTitleCard.Location = new Point(14, 14);

            lblValue = new Label();
            lblValue.Text = "0";
            lblValue.AutoSize = true;
            lblValue.ForeColor = Color.White;
            lblValue.Font = new Font("Segoe UI", 24F, FontStyle.Bold);
            lblValue.Location = new Point(68, 36);

            panel.Controls.Add(lblTitleCard);
            panel.Controls.Add(lblValue);
            return panel;
        }

        private void ReportForm_Load(object sender, EventArgs e)
        {
            LoadReport();
        }

        private void BtnLoad_Click(object sender, EventArgs e)
        {
            LoadReport();
        }

        private void LoadReport()
        {
            lblBooks.Text = DatabaseHelper.ExecuteScalar("SELECT COUNT(*) FROM Books WHERE IsActive = 1").ToString();
            lblBorrowing.Text = DatabaseHelper.ExecuteScalar("SELECT COUNT(*) FROM BorrowRecords WHERE Status = N'Đang mượn'").ToString();
            lblOverdue.Text = DatabaseHelper.ExecuteScalar("SELECT COUNT(*) FROM BorrowRecords WHERE Status = N'Đang mượn' AND DueDate < CAST(GETDATE() AS DATE)").ToString();
            lblMembers.Text = DatabaseHelper.ExecuteScalar("SELECT COUNT(*) FROM Members WHERE IsActive = 1").ToString();

            dgvTopBooks.DataSource = DatabaseHelper.ExecuteQuery(@"
                SELECT TOP 10 b.Title AS [Tên sách], COUNT(br.BorrowID) AS [Số lần mượn]
                FROM BorrowRecords br
                INNER JOIN Books b ON br.BookID = b.BookID
                WHERE br.BorrowDate >= @FromDate AND br.BorrowDate < DATEADD(DAY, 1, @ToDate)
                GROUP BY b.Title
                ORDER BY [Số lần mượn] DESC",
                new SqlParameter("@FromDate", dtFrom.Value.Date),
                new SqlParameter("@ToDate", dtTo.Value.Date));
        }
    }
}

