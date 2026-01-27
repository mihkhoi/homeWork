package lab5.bai4.ui;

import lab5.bai4.PaymentCalculator;
import lab5.bai4.PaymentHistoryRepo;

import javax.swing.*;
import java.awt.*;

public
class PaymentForm extends JFrame {
  private
    final JRadioButton rdMale = new JRadioButton("Male");
  private
    final JRadioButton rdFemale = new JRadioButton("Female");
  private
    final JRadioButton rdChild = new JRadioButton("Child (0 - 17 years)");
  private
    final JTextField txtAge = new JTextField(10);
  private
    final JTextField txtPayment = new JTextField(10);
  private
    final JButton btnCalc = new JButton("Calculate");
  private
    final PaymentHistoryRepo repo = new PaymentHistoryRepo();

  public
    PaymentForm() {
        super("Bài 4 - Calculate Payment");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(520, 260);
        setLocationRelativeTo(null);

        txtPayment.setEditable(false);

        ButtonGroup group = new ButtonGroup();
        group.add(rdMale);
        group.add(rdFemale);
        group.add(rdChild);
        rdMale.setSelected(true);

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.setBorder(BorderFactory.createTitledBorder("Patient Type"));
        typePanel.add(rdMale);
        typePanel.add(rdFemale);
        typePanel.add(rdChild);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0;
        g.gridy = 0;
        form.add(new JLabel("Age (Years):"), g);
        g.gridx = 1;
        form.add(txtAge, g);
        g.gridx = 2;
        form.add(btnCalc, g);

        g.gridx = 0;
        g.gridy = 1;
        form.add(new JLabel("Payment is:"), g);
        g.gridx = 1;
        form.add(txtPayment, g);
        g.gridx = 2;
        form.add(new JLabel("euro €"), g);

        setLayout(new BorderLayout());
        add(typePanel, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);

        btnCalc.addActionListener(e->onCalculate());
    }

  private
    void onCalculate() {
        try {
            int age = Integer.parseInt(txtAge.getText().trim());

            PaymentCalculator.Type type =
                rdMale.isSelected() ? PaymentCalculator.Type.MALE : (rdFemale.isSelected() ? PaymentCalculator.Type.FEMALE : PaymentCalculator.Type.CHILD);

            int payment = PaymentCalculator.calc(type, age);
            txtPayment.setText(String.valueOf(payment));

            // Kết nối DB: lưu lịch sử tính tiền (log)
            repo.insert(type.name(), age, payment);

            JOptionPane.showMessageDialog(this, "Tính tiền thành công!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age phải là số nguyên!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

  public
    static void main(String[] args) {
        SwingUtilities.invokeLater(()->new PaymentForm().setVisible(true));
    }
}
