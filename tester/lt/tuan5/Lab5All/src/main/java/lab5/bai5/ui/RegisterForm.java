
package lab5.bai5.ui;

import lab5.bai5.RegisterService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public
class RegisterForm extends JFrame {

  private
    final JTextField txtCustomerId = new JTextField(18);
  private
    final JTextField txtFullName = new JTextField(18);
  private
    final JTextField txtEmail = new JTextField(18);
  private
    final JTextField txtPhone = new JTextField(18);
  private
    final JTextArea txtAddress = new JTextArea(3, 18);
  private
    final JPasswordField txtPassword = new JPasswordField(18);
  private
    final JPasswordField txtConfirm = new JPasswordField(18);
  private
    final JTextField txtDob = new JTextField(18); // yyyy-mm-dd

  private
    final JRadioButton rdNam = new JRadioButton("Nam");
  private
    final JRadioButton rdNu = new JRadioButton("Nữ");
  private
    final JRadioButton rdKhac = new JRadioButton("Khác");

  private
    final JCheckBox chkTerms = new JCheckBox("Tôi đồng ý với các điều khoản dịch vụ");
  private
    final JButton btnRegister = new JButton("Đăng ký");
  private
    final JButton btnReset = new JButton("Nhập lại");

  private
    final RegisterService service = new RegisterService();

  public
    RegisterForm() {
        super("Bài 5 - Đăng ký tài khoản khách hàng");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 520);
        setLocationRelativeTo(null);

        txtAddress.setLineWrap(true);
        txtAddress.setWrapStyleWord(true);

        ButtonGroup bg = new ButtonGroup();
        bg.add(rdNam);
        bg.add(rdNu);
        bg.add(rdKhac);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.anchor = GridBagConstraints.WEST;

        int r = 0;
        addRow(p, g, r++, "Mã khách hàng:", txtCustomerId);
        addRow(p, g, r++, "Họ và tên:", txtFullName);
        addRow(p, g, r++, "Email:", txtEmail);
        addRow(p, g, r++, "Số điện thoại:", txtPhone);

        // Address (textarea)
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Địa chỉ:"), g);
        g.gridx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        p.add(new JScrollPane(txtAddress), g);
        g.fill = GridBagConstraints.NONE;
        r++;

        addRow(p, g, r++, "Mật khẩu:", txtPassword);
        addRow(p, g, r++, "Xác nhận mật khẩu:", txtConfirm);
        addRow(p, g, r++, "Ngày sinh (yyyy-mm-dd):", txtDob);

        // Gender
        g.gridx = 0;
        g.gridy = r;
        p.add(new JLabel("Giới tính:"), g);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        genderPanel.add(rdNam);
        genderPanel.add(rdNu);
        genderPanel.add(rdKhac);
        g.gridx = 1;
        p.add(genderPanel, g);
        r++;

        // Terms
        g.gridx = 1;
        g.gridy = r;
        p.add(chkTerms, g);
        r++;

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 8));
        btns.add(btnRegister);
        btns.add(btnReset);

        btnRegister.addActionListener(e->onRegister());
        btnReset.addActionListener(e->onReset());

        setLayout(new BorderLayout());
        add(p, BorderLayout.CENTER);
        add(btns, BorderLayout.SOUTH);
    }

  private
    void addRow(JPanel p, GridBagConstraints g, int row, String label, JComponent comp) {
        g.gridx = 0;
        g.gridy = row;
        p.add(new JLabel(label), g);
        g.gridx = 1;
        p.add(comp, g);
    }

  private
    void onRegister() {
        try {
            LocalDate dob = null;
            String dobTxt = txtDob.getText().trim();
            if (!dobTxt.isEmpty())
                dob = LocalDate.parse(dobTxt);

            String gender = null;
            if (rdNam.isSelected())
                gender = "Nam";
            else if (rdNu.isSelected())
                gender = "Nữ";
            else if (rdKhac.isSelected())
                gender = "Khác";

            String err = service.register(
                txtCustomerId.getText().trim(),
                txtFullName.getText().trim(),
                txtEmail.getText().trim(),
                txtPhone.getText().trim(),
                txtAddress.getText().trim(),
                new String(txtPassword.getPassword()),
                new String(txtConfirm.getPassword()),
                dob,
                gender,
                chkTerms.isSelected());

            if (err == null) {
                JOptionPane.showMessageDialog(this, "Đăng ký tài khoản thành công!");
                onReset();
            } else {
                JOptionPane.showMessageDialog(this, err);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

  private
    void onReset() {
        txtCustomerId.setText("");
        txtFullName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtPassword.setText("");
        txtConfirm.setText("");
        txtDob.setText("");
        rdNam.setSelected(false);
        rdNu.setSelected(false);
        rdKhac.setSelected(false);
        chkTerms.setSelected(false);
    }

  public
    static void main(String[] args) {
        SwingUtilities.invokeLater(()->new RegisterForm().setVisible(true));
    }
}
