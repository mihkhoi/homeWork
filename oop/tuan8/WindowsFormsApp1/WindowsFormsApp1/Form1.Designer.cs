namespace WindowsFormsApp1
{
    partial class Form1
    {
        //Thực hành 1
        //private System.Windows.Forms.ListView lsvDanhSach;
        //private System.Windows.Forms.ColumnHeader colMa;
        //private System.Windows.Forms.ColumnHeader colTen;
        //private System.Windows.Forms.ColumnHeader colDiaChi;
        //private System.Windows.Forms.GroupBox gbChiTiet;
        //private System.Windows.Forms.Label lblMa;
        //private System.Windows.Forms.Label lblTen;
        //private System.Windows.Forms.Label lblDiaChi;
        //private System.Windows.Forms.TextBox txtMaNXB;
        //private System.Windows.Forms.TextBox txtTenNXB;
        //private System.Windows.Forms.TextBox txtDiaChi;
        //private System.Windows.Forms.Button btnReload;
        //private System.Windows.Forms.Button btnClose;

        //private void InitializeComponent()
        //{
        //    this.lsvDanhSach = new System.Windows.Forms.ListView();
        //    this.colMa = new System.Windows.Forms.ColumnHeader();
        //    this.colTen = new System.Windows.Forms.ColumnHeader();
        //    this.colDiaChi = new System.Windows.Forms.ColumnHeader();
        //    this.gbChiTiet = new System.Windows.Forms.GroupBox();
        //    this.txtDiaChi = new System.Windows.Forms.TextBox();
        //    this.txtTenNXB = new System.Windows.Forms.TextBox();
        //    this.txtMaNXB = new System.Windows.Forms.TextBox();
        //    this.lblDiaChi = new System.Windows.Forms.Label();
        //    this.lblTen = new System.Windows.Forms.Label();
        //    this.lblMa = new System.Windows.Forms.Label();
        //    this.btnReload = new System.Windows.Forms.Button();
        //    this.btnClose = new System.Windows.Forms.Button();
        //    this.gbChiTiet.SuspendLayout();
        //    this.SuspendLayout();
        //    // 
        //    // lsvDanhSach
        //    // 
        //    this.lsvDanhSach.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
        //this.colMa, this.colTen, this.colDiaChi});
        //    this.lsvDanhSach.FullRowSelect = true;
        //    this.lsvDanhSach.GridLines = true;
        //    this.lsvDanhSach.HideSelection = false;
        //    this.lsvDanhSach.Location = new System.Drawing.Point(0, 0);
        //    this.lsvDanhSach.MultiSelect = false;
        //    this.lsvDanhSach.Name = "lsvDanhSach";
        //    this.lsvDanhSach.Size = new System.Drawing.Size(760, 260);
        //    this.lsvDanhSach.TabIndex = 0;
        //    this.lsvDanhSach.UseCompatibleStateImageBehavior = false;
        //    this.lsvDanhSach.View = System.Windows.Forms.View.Details;
        //    this.lsvDanhSach.SelectedIndexChanged += new System.EventHandler(this.lsvDanhSach_SelectedIndexChanged);
        //    // 
        //    // colMa
        //    // 
        //    this.colMa.Text = "Mã NXB";
        //    this.colMa.Width = 100;
        //    // 
        //    // colTen
        //    // 
        //    this.colTen.Text = "Tên NXB";
        //    this.colTen.Width = 220;
        //    // 
        //    // colDiaChi
        //    // 
        //    this.colDiaChi.Text = "Địa chỉ";
        //    this.colDiaChi.Width = 320;
        //    // 
        //    // gbChiTiet
        //    // 
        //    this.gbChiTiet.Controls.Add(this.txtDiaChi);
        //    this.gbChiTiet.Controls.Add(this.txtTenNXB);
        //    this.gbChiTiet.Controls.Add(this.txtMaNXB);
        //    this.gbChiTiet.Controls.Add(this.lblDiaChi);
        //    this.gbChiTiet.Controls.Add(this.lblTen);
        //    this.gbChiTiet.Controls.Add(this.lblMa);
        //    this.gbChiTiet.Location = new System.Drawing.Point(12, 270);
        //    this.gbChiTiet.Name = "gbChiTiet";
        //    this.gbChiTiet.Size = new System.Drawing.Size(736, 130);
        //    this.gbChiTiet.TabIndex = 1;
        //    this.gbChiTiet.TabStop = false;
        //    this.gbChiTiet.Text = "Chi tiết NXB";
        //    // 
        //    // txtDiaChi
        //    // 
        //    this.txtDiaChi.Location = new System.Drawing.Point(92, 80);
        //    this.txtDiaChi.Name = "txtDiaChi";
        //    this.txtDiaChi.ReadOnly = true;
        //    this.txtDiaChi.Size = new System.Drawing.Size(620, 22);
        //    this.txtDiaChi.TabIndex = 5;
        //    // 
        //    // txtTenNXB
        //    // 
        //    this.txtTenNXB.Location = new System.Drawing.Point(356, 34);
        //    this.txtTenNXB.Name = "txtTenNXB";
        //    this.txtTenNXB.ReadOnly = true;
        //    this.txtTenNXB.Size = new System.Drawing.Size(356, 22);
        //    this.txtTenNXB.TabIndex = 4;
        //    // 
        //    // txtMaNXB
        //    // 
        //    this.txtMaNXB.Location = new System.Drawing.Point(92, 34);
        //    this.txtMaNXB.Name = "txtMaNXB";
        //    this.txtMaNXB.ReadOnly = true;
        //    this.txtMaNXB.Size = new System.Drawing.Size(150, 22);
        //    this.txtMaNXB.TabIndex = 3;
        //    // 
        //    // lblDiaChi
        //    // 
        //    this.lblDiaChi.AutoSize = true;
        //    this.lblDiaChi.Location = new System.Drawing.Point(20, 83);
        //    this.lblDiaChi.Name = "lblDiaChi";
        //    this.lblDiaChi.Size = new System.Drawing.Size(54, 16);
        //    this.lblDiaChi.TabIndex = 2;
        //    this.lblDiaChi.Text = "Địa chỉ:";
        //    // 
        //    // lblTen
        //    // 
        //    this.lblTen.AutoSize = true;
        //    this.lblTen.Location = new System.Drawing.Point(280, 37);
        //    this.lblTen.Name = "lblTen";
        //    this.lblTen.Size = new System.Drawing.Size(63, 16);
        //    this.lblTen.TabIndex = 1;
        //    this.lblTen.Text = "Tên NXB:";
        //    // 
        //    // lblMa
        //    // 
        //    this.lblMa.AutoSize = true;
        //    this.lblMa.Location = new System.Drawing.Point(20, 37);
        //    this.lblMa.Name = "lblMa";
        //    this.lblMa.Size = new System.Drawing.Size(57, 16);
        //    this.lblMa.TabIndex = 0;
        //    this.lblMa.Text = "Mã NXB:";
        //    // 
        //    // btnReload
        //    // 
        //    this.btnReload.Location = new System.Drawing.Point(12, 415);
        //    this.btnReload.Name = "btnReload";
        //    this.btnReload.Size = new System.Drawing.Size(100, 32);
        //    this.btnReload.TabIndex = 2;
        //    this.btnReload.Text = "Tải lại";
        //    this.btnReload.UseVisualStyleBackColor = true;
        //    this.btnReload.Click += (s, e) => { HienThiDanhSachNXB(); };
        //    // 
        //    // btnClose
        //    // 
        //    this.btnClose.Location = new System.Drawing.Point(118, 415);
        //    this.btnClose.Name = "btnClose";
        //    this.btnClose.Size = new System.Drawing.Size(100, 32);
        //    this.btnClose.TabIndex = 3;
        //    this.btnClose.Text = "Đóng";
        //    this.btnClose.UseVisualStyleBackColor = true;
        //    this.btnClose.Click += (s, e) => { this.Close(); };
        //    // 
        //    // Form1
        //    // 
        //    this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
        //    this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        //    this.ClientSize = new System.Drawing.Size(760, 460);
        //    this.Controls.Add(this.btnClose);
        //    this.Controls.Add(this.btnReload);
        //    this.Controls.Add(this.gbChiTiet);
        //    this.Controls.Add(this.lsvDanhSach);
        //    this.Name = "Form1";
        //    this.Text = "Quản lý Nhà Xuất Bản";
        //    this.Load += new System.EventHandler(this.Form1_Load);
        //    this.gbChiTiet.ResumeLayout(false);
        //    this.gbChiTiet.PerformLayout();
        //    this.ResumeLayout(false);
        //}


        //Thực hành 2
        /// <summary>Required designer variable.</summary>
        //private System.ComponentModel.IContainer components = null;

        ///// <summary>Clean up any resources being used.</summary>
        //protected override void Dispose(bool disposing)
        //{
        //    if (disposing && (components != null))
        //        components.Dispose();
        //    base.Dispose(disposing);
        //}

        //#region Windows Form Designer generated code

        //private void InitializeComponent()
        //{
        //    this.lblTitle = new System.Windows.Forms.Label();
        //    this.lblDanhSach = new System.Windows.Forms.Label();
        //    this.lblNhapLieu = new System.Windows.Forms.Label();
        //    this.lsvDanhSach = new System.Windows.Forms.ListView();
        //    this.colMa = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
        //    this.colTen = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
        //    this.colDiaChi = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
        //    this.pnlSplit = new System.Windows.Forms.Panel();
        //    this.grpNhapLieu = new System.Windows.Forms.GroupBox();
        //    this.txtDiaChi = new System.Windows.Forms.TextBox();
        //    this.txtTenNXB = new System.Windows.Forms.TextBox();
        //    this.txtMaNXB = new System.Windows.Forms.TextBox();
        //    this.lblDiaChi = new System.Windows.Forms.Label();
        //    this.lblTen = new System.Windows.Forms.Label();
        //    this.lblMa = new System.Windows.Forms.Label();
        //    this.btnThemDL = new System.Windows.Forms.Button();
        //    this.grpNhapLieu.SuspendLayout();
        //    this.SuspendLayout();
        //    // 
        //    // lblTitle
        //    // 
        //    this.lblTitle.Dock = System.Windows.Forms.DockStyle.Top;
        //    this.lblTitle.Font = new System.Drawing.Font("Segoe UI", 18F, System.Drawing.FontStyle.Bold);
        //    this.lblTitle.Location = new System.Drawing.Point(0, 0);
        //    this.lblTitle.Name = "lblTitle";
        //    this.lblTitle.Size = new System.Drawing.Size(820, 58);
        //    this.lblTitle.TabIndex = 0;
        //    this.lblTitle.Text = "Thêm dữ liệu";
        //    this.lblTitle.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
        //    // 
        //    // lblDanhSach
        //    // 
        //    this.lblDanhSach.AutoSize = true;
        //    this.lblDanhSach.Location = new System.Drawing.Point(12, 67);
        //    this.lblDanhSach.Name = "lblDanhSach";
        //    this.lblDanhSach.Size = new System.Drawing.Size(146, 15);
        //    this.lblDanhSach.TabIndex = 1;
        //    this.lblDanhSach.Text = "Danh sách nhà xuất bản:";
        //    // 
        //    // lblNhapLieu
        //    // 
        //    this.lblNhapLieu.AutoSize = true;
        //    this.lblNhapLieu.Location = new System.Drawing.Point(472, 67);
        //    this.lblNhapLieu.Name = "lblNhapLieu";
        //    this.lblNhapLieu.Size = new System.Drawing.Size(120, 15);
        //    this.lblNhapLieu.TabIndex = 2;
        //    this.lblNhapLieu.Text = "Thông tin nhập liệu:";
        //    // 
        //    // lsvDanhSach
        //    // 
        //    this.lsvDanhSach.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
        //    this.colMa,
        //    this.colTen,
        //    this.colDiaChi});
        //    this.lsvDanhSach.FullRowSelect = true;
        //    this.lsvDanhSach.GridLines = true;
        //    this.lsvDanhSach.HideSelection = false;
        //    this.lsvDanhSach.Location = new System.Drawing.Point(12, 90);
        //    this.lsvDanhSach.MultiSelect = false;
        //    this.lsvDanhSach.Name = "lsvDanhSach";
        //    this.lsvDanhSach.Size = new System.Drawing.Size(432, 340);
        //    this.lsvDanhSach.TabIndex = 3;
        //    this.lsvDanhSach.UseCompatibleStateImageBehavior = false;
        //    this.lsvDanhSach.View = System.Windows.Forms.View.Details;
        //    // 
        //    // colMa
        //    // 
        //    this.colMa.Text = "Mã NXB";
        //    this.colMa.Width = 90;
        //    // 
        //    // colTen
        //    // 
        //    this.colTen.Text = "Tên NXB";
        //    this.colTen.Width = 160;
        //    // 
        //    // colDiaChi
        //    // 
        //    this.colDiaChi.Text = "Địa chỉ";
        //    this.colDiaChi.Width = 160;
        //    // 
        //    // pnlSplit
        //    // 
        //    this.pnlSplit.BackColor = System.Drawing.Color.Silver;
        //    this.pnlSplit.Location = new System.Drawing.Point(450, 66);
        //    this.pnlSplit.Name = "pnlSplit";
        //    this.pnlSplit.Size = new System.Drawing.Size(2, 370);
        //    this.pnlSplit.TabIndex = 4;
        //    // 
        //    // grpNhapLieu
        //    // 
        //    this.grpNhapLieu.Controls.Add(this.txtDiaChi);
        //    this.grpNhapLieu.Controls.Add(this.txtTenNXB);
        //    this.grpNhapLieu.Controls.Add(this.txtMaNXB);
        //    this.grpNhapLieu.Controls.Add(this.lblDiaChi);
        //    this.grpNhapLieu.Controls.Add(this.lblTen);
        //    this.grpNhapLieu.Controls.Add(this.lblMa);
        //    this.grpNhapLieu.Location = new System.Drawing.Point(475, 90);
        //    this.grpNhapLieu.Name = "grpNhapLieu";
        //    this.grpNhapLieu.Size = new System.Drawing.Size(333, 205);
        //    this.grpNhapLieu.TabIndex = 5;
        //    this.grpNhapLieu.TabStop = false;
        //    // 
        //    // txtDiaChi
        //    // 
        //    this.txtDiaChi.Location = new System.Drawing.Point(98, 120);
        //    this.txtDiaChi.Name = "txtDiaChi";
        //    this.txtDiaChi.Size = new System.Drawing.Size(215, 23);
        //    this.txtDiaChi.TabIndex = 5;
        //    // 
        //    // txtTenNXB
        //    // 
        //    this.txtTenNXB.Location = new System.Drawing.Point(98, 75);
        //    this.txtTenNXB.Name = "txtTenNXB";
        //    this.txtTenNXB.Size = new System.Drawing.Size(215, 23);
        //    this.txtTenNXB.TabIndex = 3;
        //    // 
        //    // txtMaNXB
        //    // 
        //    this.txtMaNXB.Location = new System.Drawing.Point(98, 30);
        //    this.txtMaNXB.MaxLength = 10;
        //    this.txtMaNXB.Name = "txtMaNXB";
        //    this.txtMaNXB.Size = new System.Drawing.Size(215, 23);
        //    this.txtMaNXB.TabIndex = 1;
        //    // 
        //    // lblDiaChi
        //    // 
        //    this.lblDiaChi.AutoSize = true;
        //    this.lblDiaChi.Location = new System.Drawing.Point(22, 123);
        //    this.lblDiaChi.Name = "lblDiaChi";
        //    this.lblDiaChi.Size = new System.Drawing.Size(50, 15);
        //    this.lblDiaChi.TabIndex = 4;
        //    this.lblDiaChi.Text = "Địa chỉ:";
        //    // 
        //    // lblTen
        //    // 
        //    this.lblTen.AutoSize = true;
        //    this.lblTen.Location = new System.Drawing.Point(22, 78);
        //    this.lblTen.Name = "lblTen";
        //    this.lblTen.Size = new System.Drawing.Size(59, 15);
        //    this.lblTen.TabIndex = 2;
        //    this.lblTen.Text = "Tên NXB:";
        //    // 
        //    // lblMa
        //    // 
        //    this.lblMa.AutoSize = true;
        //    this.lblMa.Location = new System.Drawing.Point(22, 33);
        //    this.lblMa.Name = "lblMa";
        //    this.lblMa.Size = new System.Drawing.Size(53, 15);
        //    this.lblMa.TabIndex = 0;
        //    this.lblMa.Text = "Mã NXB:";
        //    // 
        //    // btnThemDL
        //    // 
        //    this.btnThemDL.BackColor = System.Drawing.Color.Honeydew;
        //    this.btnThemDL.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
        //    this.btnThemDL.Location = new System.Drawing.Point(475, 312);
        //    this.btnThemDL.Name = "btnThemDL";
        //    this.btnThemDL.Size = new System.Drawing.Size(333, 40);
        //    this.btnThemDL.TabIndex = 6;
        //    this.btnThemDL.Text = "Thêm nhà xuất bản";
        //    this.btnThemDL.UseVisualStyleBackColor = false;
        //    // 
        //    // Form1
        //    // 
        //    this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
        //    this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        //    this.ClientSize = new System.Drawing.Size(820, 450);
        //    this.Controls.Add(this.btnThemDL);
        //    this.Controls.Add(this.grpNhapLieu);
        //    this.Controls.Add(this.pnlSplit);
        //    this.Controls.Add(this.lsvDanhSach);
        //    this.Controls.Add(this.lblNhapLieu);
        //    this.Controls.Add(this.lblDanhSach);
        //    this.Controls.Add(this.lblTitle);
        //    this.Name = "Form1";
        //    this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
        //    this.Text = "Form1";
        //    this.grpNhapLieu.ResumeLayout(false);
        //    this.grpNhapLieu.PerformLayout();
        //    this.ResumeLayout(false);
        //    this.PerformLayout();
        //}

        //#endregion

        //private System.Windows.Forms.Label lblTitle;
        //private System.Windows.Forms.Label lblDanhSach;
        //private System.Windows.Forms.Label lblNhapLieu;
        //private System.Windows.Forms.ListView lsvDanhSach;
        //private System.Windows.Forms.ColumnHeader colMa;
        //private System.Windows.Forms.ColumnHeader colTen;
        //private System.Windows.Forms.ColumnHeader colDiaChi;
        //private System.Windows.Forms.Panel pnlSplit;
        //private System.Windows.Forms.GroupBox grpNhapLieu;
        //private System.Windows.Forms.TextBox txtDiaChi;
        //private System.Windows.Forms.TextBox txtTenNXB;
        //private System.Windows.Forms.TextBox txtMaNXB;
        //private System.Windows.Forms.Label lblDiaChi;
        //private System.Windows.Forms.Label lblTen;
        //private System.Windows.Forms.Label lblMa;
        //private System.Windows.Forms.Button btnThemDL;



        //Thực hành 3
        //private System.ComponentModel.IContainer components = null;

        //private System.Windows.Forms.Label lblTitle;
        //private System.Windows.Forms.Label lblDanhSach;
        //private System.Windows.Forms.Label lblNhapLieu;
        //private System.Windows.Forms.ListView lsvDanhSach;
        //private System.Windows.Forms.ColumnHeader colMa;
        //private System.Windows.Forms.ColumnHeader colTen;
        //private System.Windows.Forms.ColumnHeader colDiaChi;
        //private System.Windows.Forms.Panel pnlSplit;
        //private System.Windows.Forms.GroupBox grpNhapLieu;
        //private System.Windows.Forms.TextBox txtMaNXB;
        //private System.Windows.Forms.TextBox txtTenNXB;
        //private System.Windows.Forms.TextBox txtDiaChi;
        //private System.Windows.Forms.Label lblMa;
        //private System.Windows.Forms.Label lblTen;
        //private System.Windows.Forms.Label lblDiaChi;
        //private System.Windows.Forms.Button btnCapNhat;

        //protected override void Dispose(bool disposing)
        //{
        //    if (disposing && (components != null)) components.Dispose();
        //    base.Dispose(disposing);
        //}

        //#region Windows Form Designer generated code
        //private void InitializeComponent()
        //{
        //    this.lblTitle = new System.Windows.Forms.Label();
        //    this.lblDanhSach = new System.Windows.Forms.Label();
        //    this.lblNhapLieu = new System.Windows.Forms.Label();
        //    this.lsvDanhSach = new System.Windows.Forms.ListView();
        //    this.colMa = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
        //    this.colTen = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
        //    this.colDiaChi = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
        //    this.pnlSplit = new System.Windows.Forms.Panel();
        //    this.grpNhapLieu = new System.Windows.Forms.GroupBox();
        //    this.txtDiaChi = new System.Windows.Forms.TextBox();
        //    this.txtTenNXB = new System.Windows.Forms.TextBox();
        //    this.txtMaNXB = new System.Windows.Forms.TextBox();
        //    this.lblDiaChi = new System.Windows.Forms.Label();
        //    this.lblTen = new System.Windows.Forms.Label();
        //    this.lblMa = new System.Windows.Forms.Label();
        //    this.btnCapNhat = new System.Windows.Forms.Button();
        //    this.grpNhapLieu.SuspendLayout();
        //    this.SuspendLayout();
        //    // 
        //    // lblTitle
        //    // 
        //    this.lblTitle.Dock = System.Windows.Forms.DockStyle.Top;
        //    this.lblTitle.Font = new System.Drawing.Font("Segoe UI", 18F, System.Drawing.FontStyle.Bold);
        //    this.lblTitle.Location = new System.Drawing.Point(0, 0);
        //    this.lblTitle.Name = "lblTitle";
        //    this.lblTitle.Size = new System.Drawing.Size(820, 58);
        //    this.lblTitle.TabIndex = 0;
        //    this.lblTitle.Text = "Cập nhật dữ liệu";
        //    this.lblTitle.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
        //    // 
        //    // lblDanhSach
        //    // 
        //    this.lblDanhSach.AutoSize = true;
        //    this.lblDanhSach.Location = new System.Drawing.Point(12, 67);
        //    this.lblDanhSach.Name = "lblDanhSach";
        //    this.lblDanhSach.Size = new System.Drawing.Size(146, 15);
        //    this.lblDanhSach.TabIndex = 1;
        //    this.lblDanhSach.Text = "Danh sách nhà xuất bản:";
        //    // 
        //    // lblNhapLieu
        //    // 
        //    this.lblNhapLieu.AutoSize = true;
        //    this.lblNhapLieu.Location = new System.Drawing.Point(472, 67);
        //    this.lblNhapLieu.Name = "lblNhapLieu";
        //    this.lblNhapLieu.Size = new System.Drawing.Size(120, 15);
        //    this.lblNhapLieu.TabIndex = 2;
        //    this.lblNhapLieu.Text = "Thông tin nhập liệu:";
        //    // 
        //    // lsvDanhSach
        //    // 
        //    this.lsvDanhSach.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
        //    this.colMa,
        //    this.colTen,
        //    this.colDiaChi});
        //    this.lsvDanhSach.FullRowSelect = true;
        //    this.lsvDanhSach.GridLines = true;
        //    this.lsvDanhSach.HideSelection = false;
        //    this.lsvDanhSach.Location = new System.Drawing.Point(12, 90);
        //    this.lsvDanhSach.MultiSelect = false;
        //    this.lsvDanhSach.Name = "lsvDanhSach";
        //    this.lsvDanhSach.Size = new System.Drawing.Size(432, 340);
        //    this.lsvDanhSach.TabIndex = 3;
        //    this.lsvDanhSach.UseCompatibleStateImageBehavior = false;
        //    this.lsvDanhSach.View = System.Windows.Forms.View.Details;
        //    // 
        //    // colMa
        //    // 
        //    this.colMa.Text = "Mã NXB";
        //    this.colMa.Width = 90;
        //    // 
        //    // colTen
        //    // 
        //    this.colTen.Text = "Tên NXB";
        //    this.colTen.Width = 160;
        //    // 
        //    // colDiaChi
        //    // 
        //    this.colDiaChi.Text = "Địa chỉ";
        //    this.colDiaChi.Width = 160;
        //    // 
        //    // pnlSplit
        //    // 
        //    this.pnlSplit.BackColor = System.Drawing.Color.Silver;
        //    this.pnlSplit.Location = new System.Drawing.Point(450, 66);
        //    this.pnlSplit.Name = "pnlSplit";
        //    this.pnlSplit.Size = new System.Drawing.Size(2, 370);
        //    this.pnlSplit.TabIndex = 4;
        //    // 
        //    // grpNhapLieu
        //    // 
        //    this.grpNhapLieu.Controls.Add(this.txtDiaChi);
        //    this.grpNhapLieu.Controls.Add(this.txtTenNXB);
        //    this.grpNhapLieu.Controls.Add(this.txtMaNXB);
        //    this.grpNhapLieu.Controls.Add(this.lblDiaChi);
        //    this.grpNhapLieu.Controls.Add(this.lblTen);
        //    this.grpNhapLieu.Controls.Add(this.lblMa);
        //    this.grpNhapLieu.Location = new System.Drawing.Point(475, 90);
        //    this.grpNhapLieu.Name = "grpNhapLieu";
        //    this.grpNhapLieu.Size = new System.Drawing.Size(333, 205);
        //    this.grpNhapLieu.TabIndex = 5;
        //    this.grpNhapLieu.TabStop = false;
        //    // 
        //    // txtDiaChi
        //    // 
        //    this.txtDiaChi.Location = new System.Drawing.Point(98, 120);
        //    this.txtDiaChi.Name = "txtDiaChi";
        //    this.txtDiaChi.Size = new System.Drawing.Size(215, 23);
        //    this.txtDiaChi.TabIndex = 5;
        //    // 
        //    // txtTenNXB
        //    // 
        //    this.txtTenNXB.Location = new System.Drawing.Point(98, 75);
        //    this.txtTenNXB.Name = "txtTenNXB";
        //    this.txtTenNXB.Size = new System.Drawing.Size(215, 23);
        //    this.txtTenNXB.TabIndex = 3;
        //    // 
        //    // txtMaNXB
        //    // 
        //    this.txtMaNXB.Location = new System.Drawing.Point(98, 30);
        //    this.txtMaNXB.MaxLength = 10;
        //    this.txtMaNXB.Name = "txtMaNXB";
        //    this.txtMaNXB.ReadOnly = true;   // cập nhật theo dòng chọn
        //    this.txtMaNXB.Size = new System.Drawing.Size(215, 23);
        //    this.txtMaNXB.TabIndex = 1;
        //    // 
        //    // lblDiaChi
        //    // 
        //    this.lblDiaChi.AutoSize = true;
        //    this.lblDiaChi.Location = new System.Drawing.Point(22, 123);
        //    this.lblDiaChi.Name = "lblDiaChi";
        //    this.lblDiaChi.Size = new System.Drawing.Size(50, 15);
        //    this.lblDiaChi.TabIndex = 4;
        //    this.lblDiaChi.Text = "Địa chỉ:";
        //    // 
        //    // lblTen
        //    // 
        //    this.lblTen.AutoSize = true;
        //    this.lblTen.Location = new System.Drawing.Point(22, 78);
        //    this.lblTen.Name = "lblTen";
        //    this.lblTen.Size = new System.Drawing.Size(59, 15);
        //    this.lblTen.TabIndex = 2;
        //    this.lblTen.Text = "Tên NXB:";
        //    // 
        //    // lblMa
        //    // 
        //    this.lblMa.AutoSize = true;
        //    this.lblMa.Location = new System.Drawing.Point(22, 33);
        //    this.lblMa.Name = "lblMa";
        //    this.lblMa.Size = new System.Drawing.Size(53, 15);
        //    this.lblMa.TabIndex = 0;
        //    this.lblMa.Text = "Mã NXB:";
        //    // 
        //    // btnCapNhat
        //    // 
        //    this.btnCapNhat.BackColor = System.Drawing.Color.Gainsboro;
        //    this.btnCapNhat.Enabled = false;
        //    this.btnCapNhat.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
        //    this.btnCapNhat.Location = new System.Drawing.Point(475, 312);
        //    this.btnCapNhat.Name = "btnCapNhat";
        //    this.btnCapNhat.Size = new System.Drawing.Size(333, 40);
        //    this.btnCapNhat.TabIndex = 6;
        //    this.btnCapNhat.Text = "Cập nhật thông tin";
        //    this.btnCapNhat.UseVisualStyleBackColor = false;
        //    // 
        //    // Form1
        //    // 
        //    this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
        //    this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        //    this.ClientSize = new System.Drawing.Size(820, 450);
        //    this.Controls.Add(this.btnCapNhat);
        //    this.Controls.Add(this.grpNhapLieu);
        //    this.Controls.Add(this.pnlSplit);
        //    this.Controls.Add(this.lsvDanhSach);
        //    this.Controls.Add(this.lblNhapLieu);
        //    this.Controls.Add(this.lblDanhSach);
        //    this.Controls.Add(this.lblTitle);
        //    this.Name = "Form1";
        //    this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
        //    this.Text = "Cập nhật NXB";
        //    this.grpNhapLieu.ResumeLayout(false);
        //    this.grpNhapLieu.PerformLayout();
        //    this.ResumeLayout(false);
        //    this.PerformLayout();
        //}
        //#endregion



        //Thực hành 4
        private System.ComponentModel.IContainer components = null;

        private System.Windows.Forms.Label lblTitle;
        private System.Windows.Forms.Label lblDanhSach;
        private System.Windows.Forms.ListView lsvDanhSach;
        private System.Windows.Forms.ColumnHeader colMa;
        private System.Windows.Forms.ColumnHeader colTen;
        private System.Windows.Forms.ColumnHeader colDiaChi;
        private System.Windows.Forms.Button btnXoa;

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null)) components.Dispose();
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code
        private void InitializeComponent()
        {
            this.lblTitle = new System.Windows.Forms.Label();
            this.lblDanhSach = new System.Windows.Forms.Label();
            this.lsvDanhSach = new System.Windows.Forms.ListView();
            this.colMa = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.colTen = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.colDiaChi = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.btnXoa = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // lblTitle
            // 
            this.lblTitle.Dock = System.Windows.Forms.DockStyle.Top;
            this.lblTitle.Font = new System.Drawing.Font("Segoe UI", 18F, System.Drawing.FontStyle.Bold);
            this.lblTitle.Location = new System.Drawing.Point(0, 0);
            this.lblTitle.Name = "lblTitle";
            this.lblTitle.Size = new System.Drawing.Size(700, 58);
            this.lblTitle.TabIndex = 0;
            this.lblTitle.Text = "Xóa dữ liệu";
            this.lblTitle.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // lblDanhSach
            // 
            this.lblDanhSach.AutoSize = true;
            this.lblDanhSach.Location = new System.Drawing.Point(12, 67);
            this.lblDanhSach.Name = "lblDanhSach";
            this.lblDanhSach.Size = new System.Drawing.Size(146, 15);
            this.lblDanhSach.TabIndex = 1;
            this.lblDanhSach.Text = "Danh sách nhà xuất bản:";
            // 
            // lsvDanhSach
            // 
            this.lsvDanhSach.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.colMa,
            this.colTen,
            this.colDiaChi});
            this.lsvDanhSach.FullRowSelect = true;
            this.lsvDanhSach.GridLines = true;
            this.lsvDanhSach.HideSelection = false;
            this.lsvDanhSach.Location = new System.Drawing.Point(12, 90);
            this.lsvDanhSach.MultiSelect = false;
            this.lsvDanhSach.Name = "lsvDanhSach";
            this.lsvDanhSach.Size = new System.Drawing.Size(676, 270);
            this.lsvDanhSach.TabIndex = 2;
            this.lsvDanhSach.UseCompatibleStateImageBehavior = false;
            this.lsvDanhSach.View = System.Windows.Forms.View.Details;
            // 
            // colMa
            // 
            this.colMa.Text = "Mã NXB";
            this.colMa.Width = 120;
            // 
            // colTen
            // 
            this.colTen.Text = "Tên NXB";
            this.colTen.Width = 260;
            // 
            // colDiaChi
            // 
            this.colDiaChi.Text = "Địa chỉ";
            this.colDiaChi.Width = 260;
            // 
            // btnXoa
            // 
            this.btnXoa.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.btnXoa.BackColor = System.Drawing.Color.Gainsboro;
            this.btnXoa.Enabled = false;
            this.btnXoa.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnXoa.Location = new System.Drawing.Point(12, 372);
            this.btnXoa.Name = "btnXoa";
            this.btnXoa.Size = new System.Drawing.Size(676, 40);
            this.btnXoa.TabIndex = 3;
            this.btnXoa.Text = "Xóa dữ liệu";
            this.btnXoa.UseVisualStyleBackColor = false;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(700, 430);
            this.Controls.Add(this.btnXoa);
            this.Controls.Add(this.lsvDanhSach);
            this.Controls.Add(this.lblDanhSach);
            this.Controls.Add(this.lblTitle);
            this.Name = "Form1";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Xóa NXB";
            this.ResumeLayout(false);
            this.PerformLayout();
        }
        #endregion
    }
}

