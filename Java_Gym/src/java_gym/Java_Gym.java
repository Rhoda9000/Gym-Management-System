/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_gym;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.*;
import net.proteanit.sql.DbUtils;



/**
 *
 * @author User
 */

public class Java_Gym {
    
    private JFrame frame;
    private JPanel panel;
    private JLabel label;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    
   private JTextField MemberIDTextField = new JTextField(12); 
   private JTextField NameTextField = new JTextField(12);
   private JTextField SurnameTextField = new JTextField(12);
   private JTextField DateOfBirthTextField = new JTextField(12); 
   private JTextField ID_NumTextField = new JTextField(12);
   private JTextField ContactTextField = new JTextField(12);  
   private JTextField AmountTextField = new JTextField(12);
   private JRadioButton Annual = new JRadioButton("Annual: R200/year");
   private JRadioButton Monthly = new JRadioButton("Monthly: R50/month");
    
    public Java_Gym(){
        
        frame = new JFrame("XYZ Gym");
        panel = new JPanel(new GridBagLayout());
        
        label = new JLabel("Pick an option");
        
        button1 = new JButton("Edit Gym Members");
        button1.addActionListener(new Window1());
        
        button2 = new JButton("Collect Payments");
        button2.addActionListener(new Window2());
        
        button3 = new JButton("Report Summary");
        button3.addActionListener(new Window3());
        
        GridBagConstraints c = new GridBagConstraints();
         
        c.insets = new Insets(10,10,10,10);
        panel.add(label);
       
        c.gridx = 0;
        c.gridy = 1;
        panel.add(button1,c);
        
        c.gridx = 0;
        c.gridy = 2;
        panel.add(button2,c);
       
        c.gridx = 0;
        c.gridy = 3;
        panel.add(button3,c);
        
        frame.add(panel);
    }
    
    static class Window1 implements ActionListener{
        public void actionPerformed(ActionEvent e){
            JFrame frame = new JFrame("Edit Gym members");
            frame.setVisible(true);
            frame.setSize(300, 200);
            
            JPanel panel = new JPanel();
            
            JButton Add = new JButton("Add");
            Add.addActionListener(new AddMembers());
            
            JButton Update = new JButton("Update");
            Update.addActionListener(new UpdateMembers());
            
            JButton Delete = new JButton("Delete");
            Delete.addActionListener(new DeleteMembers());
            
            frame.add(panel);
            
            panel.add(Add);
            panel.add(Update);
            panel.add(Delete);
          
        }
    }
        
    
     static class Window2 implements ActionListener{
        public void actionPerformed(ActionEvent e){
            
            Java_Gym x = new Java_Gym();
            
            JFrame frame = new JFrame("Collect Payments");
            frame.setVisible(true);
            frame.setSize(500, 400);
            
            JLabel label = new JLabel("Payments");
            
            JLabel MemberID_label = new JLabel("MemberID");
            
            JLabel Amount_label = new JLabel("Amount");
            JButton MakePayments = new JButton("MakePayment");
            
             MakePayments.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent e) {
                  try {
                      int EnterMemberID;
                      EnterMemberID = Integer.parseInt(x.MemberIDTextField.getText());
                      
                      
                      Connection con = getConnection();
                      PreparedStatement statement = con.prepareStatement("SELECT * FROM tblmembers WHERE MemberID =?");
                      
                      statement.setInt(1, EnterMemberID);
                      
                      ResultSet r1=statement.executeQuery();
                      int MemberIDCounter;
                      if(r1.next())
                      {
                          MemberIDCounter =  r1.getInt("MemberID");
                          
                          double Amount;
                          Amount = Double.parseDouble(x.AmountTextField.getText());
                          
                          JOptionPane.showMessageDialog(null , "Thank you for your payment" , "status" , 1);
                          
                          java.util.Date PayDate = new java.util.Date();
                          java.sql.Date DatePayed = new java.sql.Date(PayDate.getTime());
                                                    
                          PreparedStatement posted = con.prepareStatement("INSERT INTO tblpayments(PaymentID, Date, Amount) VALUES(?, ?, ?)");
                          
                          posted.setInt(1, EnterMemberID);
                          posted.setDate(2, DatePayed);
                          posted.setDouble(3, Amount);
                                  
                          posted.executeUpdate();
                                                                                                                            
                          x.MemberIDTextField.setText("");
                          x.AmountTextField.setText("");
                         
                          
                      }else{
                          JOptionPane.showMessageDialog(null , "Please Register" , "status" , 0);
                          
                      }     
                  } catch (Exception ex) {
                      ex.printStackTrace();
                  }
              }
             });
            
            JPanel panel = new JPanel(new GridBagLayout());
            
            GridBagConstraints c = new GridBagConstraints();
            
            c.insets = new Insets(4,4,4,4);
            
            c.gridx = 1;
            c.gridy = 0;
            panel.add(label,c);
            
            c.gridx = 0;
            c.gridy = 1;
            panel.add(MemberID_label,c);
            
            c.gridx = 1;
            c.gridy = 1;
            panel.add(x.MemberIDTextField,c);
            
            c.gridx = 0;
            c.gridy = 2;
            panel.add(Amount_label,c);
            
            c.gridx = 1;
            c.gridy = 2;
            panel.add(x.AmountTextField,c);
            
            c.gridx = 1;
            c.gridy = 5;
            panel.add(MakePayments,c);
            
            frame.add(panel);  
            
            
          
        }
    }
     
     static class Window3 implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try {
                JFrame frame = new JFrame("Report Summary");
                frame.setVisible(true);
                frame.setSize(800, 800);
                
                JPanel panel = new JPanel();
                
                ResultSet rs = null;


                Connection con = getConnection();
                PreparedStatement statement = con.prepareStatement("SELECT tblmembers.id'Order#' , tblmembers.ID_Num'ID#' , tblmembers.Name , tblmembers.Surname , tblmembers.DateOfBirth'DOB' , tblmembers.MemberType , tblpayments.Amount'AmountPaid' FROM tblmembers,tblpayments WHERE tblmembers.MemberID = tblpayments.PaymentID");
                
                rs = statement.executeQuery();
               
                
               
                JTable table = new JTable();
                
                table.setModel(DbUtils.resultSetToTableModel(rs));
                
                table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                table.setFillsViewportHeight(true);



                table.setSize(800, 800);
                table.setFillsViewportHeight(true);
                frame.setLayout(new BorderLayout());
                frame.add(table.getTableHeader(), BorderLayout.PAGE_START);             
                frame.add(table, BorderLayout.LINE_START);
                              
              
                
                frame.add(panel);
            } catch (Exception ex) {
               ex.printStackTrace();
            }
  
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        
        Java_Gym window = new Java_Gym();
        window.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.frame.setSize(400,250);
        window.frame.setVisible(true);
        
        createTable();
    }
    
    
    static class AddMembers implements ActionListener{
        public void actionPerformed(ActionEvent e){
            
            Java_Gym x = new Java_Gym();
            
            JFrame frame = new JFrame("Add Gym Members");
            JLabel label = new JLabel("Add Members");
                       
            JLabel MemberID_label = new JLabel("MemberID");           
            
            JLabel Name_label = new JLabel("Name");
            
            JLabel Surname_label = new JLabel("Surname");
                               
            JLabel DateOfBirth_label = new JLabel("DateOfBirth");
            
            JLabel ID_Num_label = new JLabel("ID Number");
                                
            JLabel Contact_label = new JLabel("Contact");
       
            JLabel MemberType_label = new JLabel("MemberType");

            
            JButton AddMember = new JButton("Add Member");
            AddMember.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent e) {
                    int MemberID;
                    MemberID = Integer.parseInt(x.MemberIDTextField.getText());
          
                    String Name = x.NameTextField.getText();
            
                    String Surname = x.SurnameTextField.getText();
            
                    DateFormat datte = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date DateOfBirth1 = new java.util.Date();
                    try {
                        DateOfBirth1 = datte.parse(x.DateOfBirthTextField.getText());
                    } catch (ParseException ex) {
                        System.out.println(ex);
                    }
                    java.sql.Date DateOfBirth = new java.sql.Date(DateOfBirth1.getTime());
                    
                    double ID_Num;
                     ID_Num = Double.parseDouble(x.ID_NumTextField.getText());

                    String Contact = x.ContactTextField.getText();

                    java.util.Date utilDate = new java.util.Date();
                    java.sql.Date DateJoined = new java.sql.Date(utilDate.getTime());

                    x.Annual.setMnemonic(KeyEvent.VK_B);
                    x.Annual.setActionCommand("Annual: R200/year");

                    x.Monthly.setMnemonic(KeyEvent.VK_B);
                    x.Monthly.setActionCommand("Monthly: R50/month");
          
            
            String MemberType = "";
            if(x.Annual.isSelected()){               
                MemberType = "Annual: R200/year";
            }else{
                if(x.Monthly.isSelected()){
                 MemberType = "Monthly: R50/month";   
                }
            }
            
             try{
            Connection con = getConnection();
            
            PreparedStatement posted = con.prepareStatement("INSERT INTO tblmembers(MemberID, Name, Surname, DateOfBirth, ID_Num, Contact, DateJoined, MemberType) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            
            posted.setInt(1, MemberID);
            posted.setString(2, Name);
            posted.setString(3, Surname);
            posted.setDate(4, DateOfBirth);
            posted.setDouble(5, ID_Num);
            posted.setString(6, Contact); 
            posted.setDate(7, DateJoined);
            posted.setString(8, MemberType); 
                
            
            posted.executeUpdate();
            
            x.NameTextField.setText("");
            x.SurnameTextField.setText("");
            x.DateOfBirthTextField.setText("");
            x.ID_NumTextField.setText("");
            x.ContactTextField.setText("");
            x.MemberIDTextField.setText("");
            x.Annual.setSelected(false);
            x.Monthly.setSelected(false);
            
        }
        catch(Exception ev){ev.printStackTrace();}
        finally{
                 JOptionPane.showMessageDialog(null , "Member Added" , "status" , 1);
        }                
         }
      });
   
            frame.setVisible(true);
            frame.setSize(650, 550);
            
            JPanel panel = new JPanel(new GridBagLayout());
            
            GridBagConstraints c = new GridBagConstraints();
            
            c.insets = new Insets(4,4,4,4);
            
            c.gridx = 1;
            c.gridy = 0;
            panel.add(label,c);
            
            c.gridx = 0;
            c.gridy = 1;
            panel.add(MemberID_label,c);
            
            c.gridx = 1;
            c.gridy = 1;
            panel.add(x.MemberIDTextField,c);
            
            c.gridx = 0;
            c.gridy = 2;
            panel.add(Name_label,c);
            
            c.gridx = 1;
            c.gridy = 2;
            panel.add(x.NameTextField,c);
            
            c.gridx = 0;
            c.gridy = 3;
            panel.add(Surname_label,c);
            
            c.gridx = 1;
            c.gridy = 3;
            panel.add(x.SurnameTextField,c);
            
            c.gridx = 0;
            c.gridy = 4;
            panel.add(DateOfBirth_label,c);
            
            c.gridx = 1;
            c.gridy = 4;
            panel.add(x.DateOfBirthTextField,c);
            
            c.gridx = 0;
            c.gridy = 5;
            panel.add(ID_Num_label,c);
            
            c.gridx = 1;
            c.gridy = 5;
            panel.add(x.ID_NumTextField,c);
            
            c.gridx = 0;
            c.gridy = 6;
            panel.add(Contact_label,c);
            
            c.gridx = 1;
            c.gridy = 6;
            panel.add(x.ContactTextField,c);
         
            c.gridx = 0;
            c.gridy = 8;
            panel.add(MemberType_label,c);
            
            c.gridx = 1;
            c.gridy = 7;
            panel.add(x.Annual,c);
            
            c.gridx = 1;
            c.gridy = 8;
            panel.add(x.Monthly,c);
            
            c.gridx = 1;
            c.gridy = 11;
            panel.add(AddMember,c);
          
            frame.add(panel);          
  
       }
    }
    
    static class DeleteMembers implements ActionListener{
        public void actionPerformed(ActionEvent e){
            Java_Gym x = new Java_Gym(); 
            
            JFrame frame = new JFrame("Delete Gym Members");
            JLabel label = new JLabel("Delete Members");
            
            JLabel MemberID_label = new JLabel("MemberID");
            
            JButton DeleteMember = new JButton("Delete Member"); 
            DeleteMember.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent e) {
                  try {
                      int MemberID;
                      MemberID = Integer.parseInt(x.MemberIDTextField.getText());
                      
                      String sql = "DELETE FROM tblmembers WHERE MemberID=?";
                        
                      Connection conn = getConnection();
                      PreparedStatement statement = conn.prepareStatement(sql);
                        
                      statement.setInt(1,MemberID);

                       int rowsDeleted = statement.executeUpdate();
                      
                        x.MemberIDTextField.setText("");
                              
                       if (rowsDeleted > 0) {
                           JOptionPane.showMessageDialog(null , "Member Deleted" , "status" , 1);
                            
                        }else{
                           JOptionPane.showMessageDialog(null , "Member does not exist" , "status" , 0);
                       }
                  } catch (Exception ex) {
                      ex.printStackTrace();
                  }
              }
            });
                       
            
            frame.setVisible(true);
            frame.setSize(500, 400);
            
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            
            c.insets = new Insets(6,6,6,6);
            
            c.gridx = 1;
            c.gridy = 0;
            panel.add(label,c);
            
            c.gridx = 0;
            c.gridy = 1;
            panel.add(MemberID_label,c);
            
            c.gridx = 1;
            c.gridy = 1;
            panel.add(x.MemberIDTextField,c);
             
            c.gridx = 1;
            c.gridy = 6;
            panel.add(DeleteMember,c);          
                       
            frame.add(panel);
            
            
        }
    }
    
    static class UpdateMembers implements ActionListener{
        public void actionPerformed(ActionEvent e){
            Java_Gym x = new Java_Gym();  
                   
            JFrame frame = new JFrame("Update Gym Members");
            JLabel label = new JLabel("Update Members");
                                   
            JLabel MemberID_label = new JLabel("MemberID");           
            
            JLabel Name_label = new JLabel("Name");
            
            JLabel Surname_label = new JLabel("Surname");
                               
            JLabel DateOfBirth_label = new JLabel("DateOfBirth");
            
            JLabel ID_Num_label = new JLabel("ID Number");
                                
            JLabel Contact_label = new JLabel("Contact");
       
            JLabel MemberType_label = new JLabel("MemberType");

            
            JButton UpdateMember = new JButton("Update Member");
            UpdateMember.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent e) {
                    int MemberID;
                    MemberID = Integer.parseInt(x.MemberIDTextField.getText());
          
                    String Name = x.NameTextField.getText();
            
                    String Surname = x.SurnameTextField.getText();
            
                    DateFormat datte = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date DateOfBirth1 = new java.util.Date();
                    try {
                        DateOfBirth1 = datte.parse(x.DateOfBirthTextField.getText());
                    } catch (ParseException ex) {
                        System.out.println(ex);
                    }
                    java.sql.Date DateOfBirth = new java.sql.Date(DateOfBirth1.getTime());
                    
                    double ID_Num;
                    ID_Num = Double.parseDouble(x.ID_NumTextField.getText());

                    String Contact = x.ContactTextField.getText();

                    
                    x.Annual.setMnemonic(KeyEvent.VK_B);
                    x.Annual.setActionCommand("Annual: R200/year");

                    x.Monthly.setMnemonic(KeyEvent.VK_B);
                    x.Monthly.setActionCommand("Monthly: R50/month");
          
            
            String MemberType = "";
            if(x.Annual.isSelected()){               
                MemberType = "Annual: R200/year";
            }else{
                if(x.Monthly.isSelected()){
                 MemberType = "Monthly: R50/month";   
                }
            }
                              
 
             try{
                 String sql = "UPDATE tblmembers SET Name = ?, Surname = ?, DateOfBirth = ?, ID_Num = ?, Contact = ?, MemberType = ? WHERE MemberID = ?";
            Connection con = getConnection();
            PreparedStatement posted = con.prepareStatement(sql);
            
            posted.setString(1,Name);
            posted.setString(2,Surname);
            posted.setDate(3,DateOfBirth);
            posted.setDouble(4,ID_Num);
            posted.setString(5,Contact);
            posted.setString(6,MemberType);
            posted.setInt(7,MemberID);
           
            posted.executeUpdate();
            
            x.NameTextField.setText("");
            x.SurnameTextField.setText("");
            x.DateOfBirthTextField.setText("");
            x.ID_NumTextField.setText("");
            x.ContactTextField.setText("");
            x.MemberIDTextField.setText("");
            x.Annual.setSelected(false);
            x.Monthly.setSelected(false);
            
        }
        catch(Exception ev){ ev.printStackTrace();}
        finally{
            JOptionPane.showMessageDialog(null , "Update Complete" , "status" , 1);
        }                
       }
     });
   
            frame.setVisible(true);
            frame.setSize(650, 550);
            
            JPanel panel = new JPanel(new GridBagLayout());
            
            GridBagConstraints c = new GridBagConstraints();
            
            c.insets = new Insets(4,4,4,4);
            
            c.gridx = 1;
            c.gridy = 0;
            panel.add(label,c);
            
            c.gridx = 0;
            c.gridy = 1;
            panel.add(MemberID_label,c);
            
            c.gridx = 1;
            c.gridy = 1;
            panel.add(x.MemberIDTextField,c);
            
            c.gridx = 0;
            c.gridy = 2;
            panel.add(Name_label,c);
            
            c.gridx = 1;
            c.gridy = 2;
            panel.add(x.NameTextField,c);
            
            c.gridx = 0;
            c.gridy = 3;
            panel.add(Surname_label,c);
            
            c.gridx = 1;
            c.gridy = 3;
            panel.add(x.SurnameTextField,c);
            
            c.gridx = 0;
            c.gridy = 4;
            panel.add(DateOfBirth_label,c);
            
            c.gridx = 1;
            c.gridy = 4;
            panel.add(x.DateOfBirthTextField,c);
            
            c.gridx = 0;
            c.gridy = 5;
            panel.add(ID_Num_label,c);
            
            c.gridx = 1;
            c.gridy = 5;
            panel.add(x.ID_NumTextField,c);
            
            c.gridx = 0;
            c.gridy = 6;
            panel.add(Contact_label,c);
            
            c.gridx = 1;
            c.gridy = 6;
            panel.add(x.ContactTextField,c);
         
            c.gridx = 0;
            c.gridy = 8;
            panel.add(MemberType_label,c);
            
            c.gridx = 1;
            c.gridy = 7;
            panel.add(x.Annual,c);
            
            c.gridx = 1;
            c.gridy = 8;
            panel.add(x.Monthly,c);
            
            c.gridx = 1;
            c.gridy = 11;
            panel.add(UpdateMember,c);
          
            frame.add(panel);      
        }
    }
    
   
    
    public static void createTable() throws Exception{
        try{
            Connection con = getConnection();
            PreparedStatement create1 = con.prepareStatement("CREATE TABLE IF NOT EXISTS tblMembers(id int NOT NULL AUTO_INCREMENT, MemberID int, Name varchar(255), Surname varchar(255), DateOfBirth date, ID_Num BIGINT, Contact varchar(255), DateJoined date, MemberType varchar(255), PRIMARY KEY(id))");
            PreparedStatement create2 = con.prepareStatement("CREATE TABLE IF NOT EXISTS tblPayments(id int NOT NULL AUTO_INCREMENT, PaymentID int, Date date, Amount double, PRIMARY KEY(id))");
            create1.executeUpdate();
            create2.executeUpdate();
        }catch(Exception e){System.out.println(e);}
        finally{
            System.out.println("Function Complete" +"\n");
        };
    }
    
    
    public static Connection getConnection() throws Exception{
        try{
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/details";
            String username = "root";
            String password = "";                    
            Class.forName(driver);
            
            Connection conn = DriverManager.getConnection(url, username, password);
            return conn;
        }catch(Exception e){e.getStackTrace();}
        
        return null;
    }
    
}
