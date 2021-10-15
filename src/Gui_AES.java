import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Gui_AES extends JFrame{

    private JPanel MainPanel;
    private JButton btnAbrir;
    private JButton BtnEncriptar;
    private JButton btnDesencriptar;
    private JButton btnGuardar;
    private JTextArea textArea1;
    private JTextArea textArea2;

    private static final String KeyAES = "klnosWO?43flbro_w?T!_Fifric8akut";
    private static final String Key2AES = "=1ez8+*Mu8L&PU?Ru@i34StEKU=867Ch";


    public Gui_AES(String titulo){
        super(titulo);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(MainPanel);
        this.setMinimumSize(new Dimension(700, 400));
        //this.setIconImage(new ImageIcon(getClass().getResource("/candado.jpg/")).getImage());
        this.setLocationRelativeTo(null);
        this.pack();

        btnAbrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbrirArchivo();
            }
        });

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuardarArchivo();
            }
        });
        BtnEncriptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Encriptar(textArea2.getText());
            }
        });
        btnDesencriptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Desencriptar(textArea2.getText());
            }
        });
    }
    public void GuardarArchivo() {

        String save = textArea2.getText();
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(MainPanel);
        File file = new File(String.valueOf(fc.getSelectedFile())+".txt");
        try {

            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(save);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AbrirArchivo() {
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(MainPanel);
        FileReader file = null;
        try {
            file = new FileReader(fc.getSelectedFile());
            BufferedReader reader = new BufferedReader(file);

            String key = "";
            String line = reader.readLine();

            while (line != null) {
                key += line + "\n";
                line = reader.readLine();
            }
            textArea1.setText(key);
            textArea2.setText(key);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void Encriptar(String data){
        data = data.replace("\n","");
        try {
            byte[] iv = new byte[16];
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(KeyAES.toCharArray(), Key2AES.getBytes(), 65536, 256);
            SecretKey secretKeyTemp = secretKeyFactory.generateSecret(keySpec);
            SecretKeySpec secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            textArea2.setText(Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("UTF-8"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Desencriptar(String data){
        data = data.replace("\n","");
        byte[] iv = new byte[16];
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(KeyAES.toCharArray(), Key2AES.getBytes(), 65536, 256);
            SecretKey secretKeyTemp = secretKeyFactory.generateSecret(keySpec);
            SecretKeySpec secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            textArea2.setText( new String(cipher.doFinal(Base64.getDecoder().decode(data))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new Gui_AES("AES encriptador y desencriptador");
        frame.setVisible(true);
    }








}
