using System;
using System.Data;
using System.Drawing;
using System.Windows.Forms;
using LibraryManagementCLean.Data;

namespace LibraryManagementCLean.Forms
{
    public class MainForm : Form
    {
        private readonly string _fullName;
        private Panel panelContent;
        private Panel panelDashboard;
        private Label lblBooks;
        private Label lblMembers;
        private Label lblBorrowing;
        private Label lblOverdue;
        private Label lblTodayBorrow;
        private Label lblTodayReturn;
        private DataGridView dgvRecent;
        private DataGridView dgvLate;
        private Label lblTime;
        private System.Windows.Forms.Timer clockTimer;

        public MainForm(string fullName)
        {
            _fullName = fullName;
            BuildUi();
            Load += MainForm_Load;
        }

        private void BuildUi()
        {
            Text = "Quản lý thư viện - Hệ thống đa máy LAN";
            WindowState = FormWindowState.Maximized;
            BackColor = Color.FromArgb(236, 239, 241);

            Panel panelTop = new Panel();
            panelTop.Dock = DockStyle.Top;
            panelTop.Height = 78;
            panelTop.BackColor = Color.FromArgb(33, 136, 198);

            Label lblTitle = new Label();
            lblTitle.Text = "📚 HỆ THỐNG QUẢN LÝ THƯ VIỆN";
            lblTitle.ForeColor = Color.White;
            lblTitle.Font = new Font("Segoe UI", 22F, FontStyle.Bold);
            lblTitle.AutoSize = true;
            lblTitle.Location = new Point(18, 16);

            Label lblUser = new Label();
            lblUser.Text = _fullName + " (Manager)";
            lblUser.ForeColor = Color.White;
            lblUser.Font = new Font("Segoe UI", 12F);
            lblUser.AutoSize = true;
            lblUser.Location = new Point(1240, 12);
            lblUser.Anchor = AnchorStyles.Top | AnchorStyles.Right;

            lblTime = new Label();
            lblTime.Text = DateTime.Now.ToString("dd/MM/yyyy HH:mm:ss");
            lblTime.ForeColor = Color.White;
            lblTime.Font = new Font("Segoe UI", 12F);
            lblTime.AutoSize = true;
            lblTime.Location = new Point(1240, 42);
            lblTime.Anchor = AnchorStyles.Top | AnchorStyles.Right;

            panelTop.Controls.Add(lblTitle);
            panelTop.Controls.Add(lblUser);
            panelTop.Controls.Add(lblTime);

            Panel panelLeft = new Panel();
            panelLeft.Dock = DockStyle.Left;
            panelLeft.Width = 180;
            panelLeft.BackColor = Color.FromArgb(43, 60, 82);

            CreateMenuButton(panelLeft, "Trang chủ", 18, BtnHome_Click, Color.FromArgb(43, 60, 82));
            CreateMenuButton(panelLeft, "Quản lý Sách", 66, BtnBooks_Click, Color.FromArgb(43, 60, 82));
            CreateMenuButton(panelLeft, "Quản lý Độc giả", 114, BtnMembers_Click, Color.FromArgb(43, 60, 82));
            CreateMenuButton(panelLeft, "Mượn sách", 162, BtnBorrow_Click, Color.FromArgb(43, 60, 82));
            CreateMenuButton(panelLeft, "Trả sách", 210, BtnReturn_Click, Color.FromArgb(43, 60, 82));
            CreateMenuButton(panelLeft, "Báo cáo  Thống kê", 258, BtnReport_Click, Color.FromArgb(43, 60, 82));
            CreateMenuButton(panelLeft, "Đăng xuất", 760, BtnLogout_Click, Color.FromArgb(192, 57, 43));

            panelContent = new Panel();
            panelContent.Dock = DockStyle.Fill;
            panelContent.BackColor = Color.FromArgb(236, 239, 241);

            panelDashboard = new Panel();
            panelDashboard.Dock = DockStyle.Fill;
            panelDashboard.BackColor = Color.FromArgb(236, 239, 241);

            panelDashboard.Controls.Add(CreateCard("Tổng số sách", Color.FromArgb(52, 152, 219), out lblBooks, 18));
            panelDashboard.Controls.Add(CreateCard("Độc giả", Color.FromArgb(46, 204, 113), out lblMembers, 208));
            panelDashboard.Controls.Add(CreateCard("Đang mượn", Color.FromArgb(155, 89, 182), out lblBorrowing, 398));
            panelDashboard.Controls.Add(CreateCard("Quá hạn", Color.FromArgb(231, 76, 60), out lblOverdue, 588));
            panelDashboard.Controls.Add(CreateCard("Mượn hôm nay", Color.FromArgb(241, 196, 15), out lblTodayBorrow, 778));
            panelDashboard.Controls.Add(CreateCard("Trả hôm nay", Color.FromArgb(26, 188, 156), out lblTodayReturn, 968));

            Label lblRecent = new Label();
            lblRecent.Text = "Mượn sách gần đây";
            lblRecent.Font = new Font("Segoe UI", 16F, FontStyle.Bold);
            lblRecent.AutoSize = true;
            lblRecent.Location = new Point(18, 132);

            dgvRecent = new DataGridView();
            dgvRecent.Location = new Point(18, 170);
            dgvRecent.Size = new Size(690, 560);
            dgvRecent.ReadOnly = true;
            dgvRecent.AllowUserToAddRows = false;
            dgvRecent.AllowUserToDeleteRows = false;
            dgvRecent.BackgroundColor = Color.White;
            dgvRecent.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dgvRecent.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dgvRecent.RowHeadersVisible = false;

            Label lblLateTitle = new Label();
            lblLateTitle.Text = "Sách quá hạn";
            lblLateTitle.Font = new Font("Segoe UI", 16F, FontStyle.Bold);
            lblLateTitle.ForeColor = Color.Red;
            lblLateTitle.AutoSize = true;
            lblLateTitle.Location = new Point(730, 132);

            dgvLate = new DataGridView();
            dgvLate.Location = new Point(730, 170);
            dgvLate.Size = new Size(620, 560);
            dgvLate.ReadOnly = true;
            dgvLate.AllowUserToAddRows = false;
            dgvLate.AllowUserToDeleteRows = false;
            dgvLate.BackgroundColor = Color.White;
            dgvLate.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dgvLate.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dgvLate.RowHeadersVisible = false;

            panelDashboard.Controls.Add(lblRecent);
            panelDashboard.Controls.Add(dgvRecent);
            panelDashboard.Controls.Add(lblLateTitle);
            panelDashboard.Controls.Add(dgvLate);

            panelContent.Controls.Add(panelDashboard);

            Controls.Add(panelContent);
            Controls.Add(panelLeft);
            Controls.Add(panelTop);
        }

        private void MainForm_Load(object sender, EventArgs e)
        {
            ShowDashboard();

            clockTimer = new System.Windows.Forms.Timer();
            clockTimer.Interval = 1000;
            clockTimer.Tick += ClockTimer_Tick;
            clockTimer.Start();
        }

        private void ClockTimer_Tick(object sender, EventArgs e)
        {
            lblTime.Text = DateTime.Now.ToString("dd/MM/yyyy HH:mm:ss");
        }

        private void CreateMenuButton(Panel panelLeft, string text, int top, EventHandler click, Color color)
        {
            Button btn = new Button();
            btn.Text = text;
            btn.Location = new Point(8, top);
            btn.Size = new Size(164, 42);
            btn.BackColor = color;
            btn.ForeColor = Color.White;
            btn.FlatStyle = FlatStyle.Flat;
            btn.Font = new Font("Segoe UI", 12F);
            btn.TextAlign = ContentAlignment.MiddleLeft;
            btn.Padding = new Padding(15, 0, 0, 0);
            btn.FlatAppearance.BorderSize = 0;
            btn.Click += click;
            panelLeft.Controls.Add(btn);
        }

        private Panel CreateCard(string title, Color color, out Label lblValue, int x)
        {
            Panel panel = new Panel();
            panel.BackColor = color;
            panel.Location = new Point(x, 18);
            panel.Size = new Size(170, 88);

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

        private void ShowDashboard()
        {
            panelContent.Controls.Clear();
            panelContent.Controls.Add(panelDashboard);
            LoadDashboard();
        }

        private void OpenChild(Form child)
        {
            child.TopLevel = false;
            child.FormBorderStyle = FormBorderStyle.None;
            child.Dock = DockStyle.Fill;

            panelContent.Controls.Clear();
            panelContent.Controls.Add(child);
            child.Show();
        }

        private void LoadDashboard()
        {
            lblBooks.Text = DatabaseHelper.ExecuteScalar("SELECT COUNT(*) FROM Books WHERE IsActive = 1").ToString();
            lblMembers.Text = DatabaseHelper.ExecuteScalar("SELECT COUNT(*) FROM Members WHERE IsActive = 1").ToString();
            lblBorrowing.Text = DatabaseHelper.ExecuteScalar("SELECT COUNT(*) FROM BorrowRecords WHERE Status = N'Đang mượn'").ToString();
            lblOverdue.Text = DatabaseHelper.ExecuteScalar("SELECT COUNT(*) FROM BorrowRecords WHERE Status = N'Đang mượn' AND DueDate < CAST(GETDATE() AS DATE)").ToString();
            lblTodayBorrow.Text = DatabaseHelper.ExecuteScalar("SELECT COUNT(*) FROM BorrowRecords WHERE BorrowDate = CAST(GETDATE() AS DATE)").ToString();
            lblTodayReturn.Text = DatabaseHelper.ExecuteScalar("SELECT COUNT(*) FROM BorrowRecords WHERE ReturnDate = CAST(GETDATE() AS DATE)").ToString();

            dgvRecent.DataSource = DatabaseHelper.ExecuteQuery(@"
                SELECT TOP 10 br.BorrowCode AS [Mã phiếu], m.FullName AS [Độc giả], b.Title AS [Tên sách],
                       br.BorrowDate AS [Ngày mượn], br.DueDate AS [Hạn trả]
                FROM BorrowRecords br
                INNER JOIN Members m ON br.MemberID = m.MemberID
                INNER JOIN Books b ON br.BookID = b.BookID
                ORDER BY br.BorrowID DESC");

            dgvLate.DataSource = DatabaseHelper.ExecuteQuery(@"
                SELECT m.FullName AS [Độc giả], b.Title AS [Tên sách], br.DueDate AS [Hạn trả],
                       DATEDIFF(DAY, br.DueDate, GETDATE()) AS [Quá hạn]
                FROM BorrowRecords br
                INNER JOIN Members m ON br.MemberID = m.MemberID
                INNER JOIN Books b ON br.BookID = b.BookID
                WHERE br.Status = N'Đang mượn' AND br.DueDate < CAST(GETDATE() AS DATE)
                ORDER BY br.DueDate");
        }

        private void BtnHome_Click(object sender, EventArgs e)
        {
            ShowDashboard();
        }

        private void BtnBooks_Click(object sender, EventArgs e)
        {
            OpenChild(new BookManagementForm());
        }

        private void BtnMembers_Click(object sender, EventArgs e)
        {
            OpenChild(new MemberManagementForm());
        }

        private void BtnBorrow_Click(object sender, EventArgs e)
        {
            OpenChild(new BorrowForm());
        }

        private void BtnReturn_Click(object sender, EventArgs e)
        {
            OpenChild(new ReturnForm());
        }

        private void BtnReport_Click(object sender, EventArgs e)
        {
            OpenChild(new ReportForm());
        }

        private void BtnLogout_Click(object sender, EventArgs e)
        {
            Close();
        }
    }
}

