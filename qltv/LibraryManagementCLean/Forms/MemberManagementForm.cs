using System;
using System.Data;
using System.Drawing;
using System.Windows.Forms;
using LibraryManagementCLean.Data;
using Microsoft.Data.SqlClient;

namespace LibraryManagementCLean.Forms
{
    public class MemberManagementForm : Form
    {
        private DataGridView dgvMembers;
        private TextBox txtSearch;
        private TextBox txtMemberCode;
        private TextBox txtFullName;
        private ComboBox cboGender;
        private DateTimePicker dtpBirthDate;
        private TextBox txtPhone;
        private TextBox txtEmail;
        private TextBox txtIdentity;
        private TextBox txtAddress;
        private ComboBox cboMemberType;
        private DateTimePicker dtpExpiryDate;
        private NumericUpDown numFineDebt;
        private TextBox txtNote;
        private int selectedMemberId;

        public MemberManagementForm()
        {
            selectedMemberId = 0;
            BuildUi();
            Load += MemberManagementForm_Load;
        }

        private void BuildUi()
        {
            BackColor = Color.FromArgb(236, 239, 241);

            Label lblTitle = new Label();
            lblTitle.Text = "QUẢN LÝ ĐỘC GIẢ";
            lblTitle.Font = new Font("Segoe UI", 22F, FontStyle.Bold);
            lblTitle.AutoSize = true;
            lblTitle.Location = new Point(20, 15);

            txtSearch = new TextBox();
            txtSearch.PlaceholderText = "Tìm theo mã, tên, SĐT...";
            txtSearch.Location = new Point(20, 60);
            txtSearch.Width = 280;

            Button btnSearch = CreateButton("Tìm", 315, 58, Color.FromArgb(33, 136, 198));
            btnSearch.Click += BtnSearch_Click;

            Button btnRefresh = CreateButton("Làm mới", 410, 58, Color.FromArgb(33, 136, 198));
            btnRefresh.Click += BtnRefresh_Click;

            dgvMembers = new DataGridView();
            dgvMembers.Location = new Point(20, 105);
            dgvMembers.Size = new Size(820, 520);
            dgvMembers.ReadOnly = true;
            dgvMembers.AllowUserToAddRows = false;
            dgvMembers.AllowUserToDeleteRows = false;
            dgvMembers.BackgroundColor = Color.White;
            dgvMembers.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            dgvMembers.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            dgvMembers.RowHeadersVisible = false;
            dgvMembers.CellClick += DgvMembers_CellClick;

            Panel panelRight = new Panel();
            panelRight.BackColor = Color.White;
            panelRight.Location = new Point(860, 60);
            panelRight.Size = new Size(430, 610);

            int y = 15;
            panelRight.Controls.Add(MakeLabel("Thông tin độc giả", 18F, 18, ref y, true));

            txtMemberCode = AddText(panelRight, "Mã thẻ", ref y);
            txtFullName = AddText(panelRight, "Họ tên", ref y);
            cboGender = AddCombo(panelRight, "Giới tính", ref y);
            dtpBirthDate = AddDate(panelRight, "Ngày sinh", ref y);
            txtPhone = AddText(panelRight, "Điện thoại", ref y);
            txtEmail = AddText(panelRight, "Email", ref y);
            txtIdentity = AddText(panelRight, "CCCD", ref y);
            txtAddress = AddText(panelRight, "Địa chỉ", ref y);
            cboMemberType = AddCombo(panelRight, "Loại thẻ", ref y);
            dtpExpiryDate = AddDate(panelRight, "Hạn thẻ", ref y);
            numFineDebt = AddNumeric(panelRight, "Nợ phạt", ref y, 0, 10000000);
            txtNote = AddMulti(panelRight, "Ghi chú", ref y);

            Button btnAdd = CreateButton("Thêm", 20, 570, Color.FromArgb(46, 204, 113));
            btnAdd.Click += BtnAdd_Click;
            Button btnUpdate = CreateButton("Sửa", 115, 570, Color.FromArgb(33, 136, 198));
            btnUpdate.Click += BtnUpdate_Click;
            Button btnDelete = CreateButton("Xóa", 210, 570, Color.FromArgb(231, 76, 60));
            btnDelete.Click += BtnDelete_Click;
            Button btnClear = CreateButton("Làm mới", 305, 570, Color.Gray);
            btnClear.Click += BtnClear_Click;

            panelRight.Controls.Add(btnAdd);
            panelRight.Controls.Add(btnUpdate);
            panelRight.Controls.Add(btnDelete);
            panelRight.Controls.Add(btnClear);

            Controls.Add(lblTitle);
            Controls.Add(txtSearch);
            Controls.Add(btnSearch);
            Controls.Add(btnRefresh);
            Controls.Add(dgvMembers);
            Controls.Add(panelRight);
        }

        private void MemberManagementForm_Load(object sender, EventArgs e)
        {
            cboGender.Items.AddRange(new object[] { "Nam", "Nữ", "Khác" });
            cboGender.SelectedIndex = 0;

            cboMemberType.Items.AddRange(new object[] { "Sinh viên", "Thường", "VIP", "Giáo viên" });
            cboMemberType.SelectedIndex = 0;

            LoadMembers(string.Empty);
        }

        private void LoadMembers(string keyword)
        {
            dgvMembers.DataSource = DatabaseHelper.ExecuteQuery(@"
                SELECT MemberID, MemberCode AS [Mã thẻ], FullName AS [Họ tên], Gender AS [Giới tính],
                       Phone AS [Điện thoại], MemberType AS [Loại thẻ], ExpiryDate AS [Hạn thẻ],
                       FineDebt AS [Nợ phạt], CASE WHEN IsActive = 1 THEN N'Hoạt động' ELSE N'Khóa' END AS [Trạng thái]
                FROM Members
                WHERE (@Keyword = N'' OR MemberCode LIKE N'%' + @Keyword + N'%' OR FullName LIKE N'%' + @Keyword + N'%' OR Phone LIKE N'%' + @Keyword + N'%')
                ORDER BY MemberID DESC",
                new SqlParameter("@Keyword", keyword));
        }

        private void BtnAdd_Click(object sender, EventArgs e)
        {
            int result = DatabaseHelper.ExecuteNonQuery(@"
                INSERT INTO Members
                (MemberCode, FullName, Gender, BirthDate, Phone, Email, IdentityNumber, Address, MemberType, JoinDate, ExpiryDate, FineDebt, Note, IsActive, CreatedDate)
                VALUES
                (@MemberCode, @FullName, @Gender, @BirthDate, @Phone, @Email, @IdentityNumber, @Address, @MemberType, CAST(GETDATE() AS DATE), @ExpiryDate, @FineDebt, @Note, 1, GETDATE())",
                new SqlParameter("@MemberCode", txtMemberCode.Text.Trim()),
                new SqlParameter("@FullName", txtFullName.Text.Trim()),
                new SqlParameter("@Gender", cboGender.Text),
                new SqlParameter("@BirthDate", dtpBirthDate.Value.Date),
                new SqlParameter("@Phone", txtPhone.Text.Trim()),
                new SqlParameter("@Email", txtEmail.Text.Trim()),
                new SqlParameter("@IdentityNumber", txtIdentity.Text.Trim()),
                new SqlParameter("@Address", txtAddress.Text.Trim()),
                new SqlParameter("@MemberType", cboMemberType.Text),
                new SqlParameter("@ExpiryDate", dtpExpiryDate.Value.Date),
                new SqlParameter("@FineDebt", Convert.ToDecimal(numFineDebt.Value)),
                new SqlParameter("@Note", txtNote.Text.Trim())
            );

            if (result > 0)
            {
                MessageBox.Show("Thêm độc giả thành công.");
                LoadMembers(string.Empty);
                ClearInput();
            }
        }

        private void BtnUpdate_Click(object sender, EventArgs e)
        {
            if (selectedMemberId == 0)
            {
                MessageBox.Show("Chọn độc giả trước.");
                return;
            }

            int result = DatabaseHelper.ExecuteNonQuery(@"
                UPDATE Members SET
                    MemberCode=@MemberCode, FullName=@FullName, Gender=@Gender, BirthDate=@BirthDate,
                    Phone=@Phone, Email=@Email, IdentityNumber=@IdentityNumber, Address=@Address,
                    MemberType=@MemberType, ExpiryDate=@ExpiryDate, FineDebt=@FineDebt, Note=@Note,
                    UpdatedDate=GETDATE()
                WHERE MemberID=@MemberID",
                new SqlParameter("@MemberCode", txtMemberCode.Text.Trim()),
                new SqlParameter("@FullName", txtFullName.Text.Trim()),
                new SqlParameter("@Gender", cboGender.Text),
                new SqlParameter("@BirthDate", dtpBirthDate.Value.Date),
                new SqlParameter("@Phone", txtPhone.Text.Trim()),
                new SqlParameter("@Email", txtEmail.Text.Trim()),
                new SqlParameter("@IdentityNumber", txtIdentity.Text.Trim()),
                new SqlParameter("@Address", txtAddress.Text.Trim()),
                new SqlParameter("@MemberType", cboMemberType.Text),
                new SqlParameter("@ExpiryDate", dtpExpiryDate.Value.Date),
                new SqlParameter("@FineDebt", Convert.ToDecimal(numFineDebt.Value)),
                new SqlParameter("@Note", txtNote.Text.Trim()),
                new SqlParameter("@MemberID", selectedMemberId)
            );

            if (result > 0)
            {
                MessageBox.Show("Cập nhật độc giả thành công.");
                LoadMembers(string.Empty);
                ClearInput();
            }
        }

        private void BtnDelete_Click(object sender, EventArgs e)
        {
            if (selectedMemberId == 0)
            {
                MessageBox.Show("Chọn độc giả trước.");
                return;
            }

            int result = DatabaseHelper.ExecuteNonQuery(
                "UPDATE Members SET IsActive = 0, UpdatedDate = GETDATE() WHERE MemberID = @MemberID",
                new SqlParameter("@MemberID", selectedMemberId));

            if (result > 0)
            {
                MessageBox.Show("Khóa thẻ độc giả thành công.");
                LoadMembers(string.Empty);
                ClearInput();
            }
        }

        private void BtnSearch_Click(object sender, EventArgs e)
        {
            LoadMembers(txtSearch.Text.Trim());
        }

        private void BtnRefresh_Click(object sender, EventArgs e)
        {
            txtSearch.Clear();
            ClearInput();
            LoadMembers(string.Empty);
        }

        private void BtnClear_Click(object sender, EventArgs e)
        {
            ClearInput();
        }

        private void DgvMembers_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex < 0 || dgvMembers.CurrentRow == null) return;

            selectedMemberId = Convert.ToInt32(dgvMembers.CurrentRow.Cells["MemberID"].Value);
            DataTable dt = DatabaseHelper.ExecuteQuery("SELECT * FROM Members WHERE MemberID = @MemberID",
                new SqlParameter("@MemberID", selectedMemberId));
            if (dt.Rows.Count == 0) return;

            DataRow row = dt.Rows[0];
            txtMemberCode.Text = row["MemberCode"].ToString();
            txtFullName.Text = row["FullName"].ToString();
            cboGender.Text = row["Gender"].ToString();
            if (row["BirthDate"] != DBNull.Value) dtpBirthDate.Value = Convert.ToDateTime(row["BirthDate"]);
            txtPhone.Text = row["Phone"].ToString();
            txtEmail.Text = row["Email"].ToString();
            txtIdentity.Text = row["IdentityNumber"].ToString();
            txtAddress.Text = row["Address"].ToString();
            cboMemberType.Text = row["MemberType"].ToString();
            if (row["ExpiryDate"] != DBNull.Value) dtpExpiryDate.Value = Convert.ToDateTime(row["ExpiryDate"]);
            numFineDebt.Value = Convert.ToDecimal(row["FineDebt"]);
            txtNote.Text = row["Note"].ToString();
        }

        private void ClearInput()
        {
            selectedMemberId = 0;
            txtMemberCode.Clear();
            txtFullName.Clear();
            txtPhone.Clear();
            txtEmail.Clear();
            txtIdentity.Clear();
            txtAddress.Clear();
            txtNote.Clear();
            numFineDebt.Value = 0;
            dtpBirthDate.Value = DateTime.Today;
            dtpExpiryDate.Value = DateTime.Today.AddYears(1);
        }

        private Label MakeLabel(string text, float size, int x, ref int y, bool bold)
        {
            Label lbl = new Label();
            lbl.Text = text;
            lbl.AutoSize = true;
            lbl.Location = new Point(x, y);
            lbl.Font = new Font("Segoe UI", size, bold ? FontStyle.Bold : FontStyle.Regular);
            y += bold ? 40 : 26;
            return lbl;
        }

        private TextBox AddText(Control parent, string caption, ref int y)
        {
            parent.Controls.Add(MakeLabel(caption, 10F, 18, ref y, false));
            TextBox txt = new TextBox();
            txt.Location = new Point(18, y);
            txt.Width = 380;
            parent.Controls.Add(txt);
            y += 36;
            return txt;
        }

        private ComboBox AddCombo(Control parent, string caption, ref int y)
        {
            parent.Controls.Add(MakeLabel(caption, 10F, 18, ref y, false));
            ComboBox cbo = new ComboBox();
            cbo.Location = new Point(18, y);
            cbo.Width = 380;
            cbo.DropDownStyle = ComboBoxStyle.DropDownList;
            parent.Controls.Add(cbo);
            y += 36;
            return cbo;
        }

        private DateTimePicker AddDate(Control parent, string caption, ref int y)
        {
            parent.Controls.Add(MakeLabel(caption, 10F, 18, ref y, false));
            DateTimePicker dt = new DateTimePicker();
            dt.Location = new Point(18, y);
            dt.Width = 380;
            dt.Format = DateTimePickerFormat.Short;
            parent.Controls.Add(dt);
            y += 36;
            return dt;
        }

        private NumericUpDown AddNumeric(Control parent, string caption, ref int y, int min, int max)
        {
            parent.Controls.Add(MakeLabel(caption, 10F, 18, ref y, false));
            NumericUpDown num = new NumericUpDown();
            num.Location = new Point(18, y);
            num.Width = 380;
            num.Minimum = min;
            num.Maximum = max;
            parent.Controls.Add(num);
            y += 36;
            return num;
        }

        private TextBox AddMulti(Control parent, string caption, ref int y)
        {
            parent.Controls.Add(MakeLabel(caption, 10F, 18, ref y, false));
            TextBox txt = new TextBox();
            txt.Location = new Point(18, y);
            txt.Width = 380;
            txt.Height = 70;
            txt.Multiline = true;
            parent.Controls.Add(txt);
            y += 85;
            return txt;
        }

        private Button CreateButton(string text, int x, int y, Color color)
        {
            Button btn = new Button();
            btn.Text = text;
            btn.Location = new Point(x, y);
            btn.Size = new Size(82, 34);
            btn.BackColor = color;
            btn.ForeColor = Color.White;
            btn.FlatStyle = FlatStyle.Flat;
            btn.FlatAppearance.BorderSize = 0;
            return btn;
        }
    }
}

