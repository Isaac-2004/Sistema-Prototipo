import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;

public class MenuLateral {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MenuLateral() {
        frame = new JFrame("Menú Lateral");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);

        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(new Color(70, 130, 180)); // Fondo azulado
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        JButton productosButton = createMenuButton("Productos");
        productosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCard("Productos");
            }
        });

        JButton clientesButton = createMenuButton("Clientes");
        clientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCard("Clientes");
            }
        });

        JButton generarFacturaButton = createMenuButton("Generar Factura");
        generarFacturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCard("Generar Factura");
            }
        });

        JButton administrarFacturaButton = createMenuButton("Administrar Factura");
        administrarFacturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCard("Administrar Factura");
            }
        });

        JButton salirButton = createMenuButton("Salir");
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        sidePanel.add(productosButton);
        sidePanel.add(clientesButton);
        sidePanel.add(generarFacturaButton);
        sidePanel.add(administrarFacturaButton);
        sidePanel.add(salirButton);

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        mainPanel.add(createProductosPanel(), "Productos");
        mainPanel.add(createClientesPanel(), "Clientes");
        mainPanel.add(createFacturaPanel(), "Generar Factura");
        mainPanel.add(createAdministrarFacturasPanel(), "Administrar Factura");

        frame.add(sidePanel, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
        return button;
    }

    private void showCard(String cardName) {
        cardLayout.show(mainPanel, cardName);
    }

    private JPanel createProductosPanel() {
        return new FormularioProductos().getPanel();
    }

    private JPanel createClientesPanel() {
        return new FormularioClientes().getPanel();
    }

    private JPanel createFacturaPanel() {
        return new FacturaApp().getPanel();
    }

    private JPanel createAdministrarFacturasPanel() {
        return new AdministrarFacturasApp().getPanel();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MenuLateral();
            }
        });
    }
}

class FormularioClientes {
    private JPanel panel;
    private JTextField nombre1Field;
    private JTextField nombre2Field;
    private JTextField apellido1Field;
    private JTextField apellido2Field;
    private JTextField identificacionField;
    private JTextField direccionField;
    private JTextField telefonoField;
    private JTextField celularField;
    private JTextField emailField;
    private JComboBox<String> tipoComboBox;
    private JComboBox<String> statusComboBox;
    private JLabel imagenLabel;
    private File imagenFile;
    private Integer currentClientId = null; // Variable para almacenar el ID del cliente actual

    private final Dimension textFieldSize = new Dimension(200, 30);
    private final Font textFieldFont = new Font("Arial", Font.PLAIN, 14);

    public FormularioClientes() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(15, 2, 10, 10));
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Primer Nombre:"));
        nombre1Field = new JTextField();
        nombre1Field.setPreferredSize(textFieldSize);
        nombre1Field.setFont(textFieldFont);
        formPanel.add(nombre1Field);

        formPanel.add(new JLabel("Segundo Nombre:"));
        nombre2Field = new JTextField();
        nombre2Field.setPreferredSize(textFieldSize);
        nombre2Field.setFont(textFieldFont);
        formPanel.add(nombre2Field);

        formPanel.add(new JLabel("Primer Apellido:"));
        apellido1Field = new JTextField();
        apellido1Field.setPreferredSize(textFieldSize);
        apellido1Field.setFont(textFieldFont);
        formPanel.add(apellido1Field);

        formPanel.add(new JLabel("Segundo Apellido:"));
        apellido2Field = new JTextField();
        apellido2Field.setPreferredSize(textFieldSize);
        apellido2Field.setFont(textFieldFont);
        formPanel.add(apellido2Field);

        formPanel.add(new JLabel("Identificación:"));
        identificacionField = new JTextField();
        identificacionField.setPreferredSize(textFieldSize);
        identificacionField.setFont(textFieldFont);
        JPanel identificacionPanel = new JPanel(new BorderLayout());
        identificacionPanel.add(identificacionField, BorderLayout.CENTER);
        JButton buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCliente();
            }
        });
        identificacionPanel.add(buscarButton, BorderLayout.EAST);
        formPanel.add(identificacionPanel);

        formPanel.add(new JLabel("Dirección:"));
        direccionField = new JTextField();
        direccionField.setPreferredSize(textFieldSize);
        direccionField.setFont(textFieldFont);
        formPanel.add(direccionField);

        formPanel.add(new JLabel("Teléfono:"));
        telefonoField = new JTextField();
        telefonoField.setPreferredSize(textFieldSize);
        telefonoField.setFont(textFieldFont);
        formPanel.add(telefonoField);

        formPanel.add(new JLabel("Celular:"));
        celularField = new JTextField();
        celularField.setPreferredSize(textFieldSize);
        celularField.setFont(textFieldFont);
        formPanel.add(celularField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        emailField.setPreferredSize(textFieldSize);
        emailField.setFont(textFieldFont);
        formPanel.add(emailField);

        formPanel.add(new JLabel("Tipo:"));
        tipoComboBox = new JComboBox<>(new String[]{"VIP", "NOR", "EMP"});
        tipoComboBox.setPreferredSize(textFieldSize);
        tipoComboBox.setFont(textFieldFont);
        formPanel.add(tipoComboBox);

        formPanel.add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(new String[]{"ACT", "INA"});
        statusComboBox.setPreferredSize(textFieldSize);
        statusComboBox.setFont(textFieldFont);
        formPanel.add(statusComboBox);

        JButton cargarImagenButton = new JButton("Cargar Imagen");
        cargarImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarImagen();
            }
        });
        formPanel.add(cargarImagenButton);

        imagenLabel = new JLabel();
        formPanel.add(imagenLabel);

        JButton guardarButton = new JButton("Guardar");
        guardarButton.setBackground(new Color(34, 139, 34)); // Verde oscuro
        guardarButton.setForeground(Color.WHITE);
        guardarButton.setFont(new Font("Arial", Font.BOLD, 14));
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarDatos(null); // Para insertar un nuevo registro
            }
        });
        formPanel.add(guardarButton);

        JButton editarButton = new JButton("Editar");
        editarButton.setBackground(new Color(255, 140, 0)); // Naranja oscuro
        editarButton.setForeground(Color.WHITE);
        editarButton.setFont(new Font("Arial", Font.BOLD, 14));
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentClientId != null) {
                    guardarDatos(currentClientId); // Para editar el registro actual
                } else {
                    JOptionPane.showMessageDialog(panel, "Por favor, busque un cliente para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        formPanel.add(editarButton);

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setBackground(new Color(178, 34, 34)); // Rojo oscuro
        cancelarButton.setForeground(Color.WHITE);
        cancelarButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        formPanel.add(cancelarButton);

        panel.add(formPanel, BorderLayout.CENTER);
    }

    private void cargarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagenFile = fileChooser.getSelectedFile();
            imagenLabel.setText(imagenFile.getName());

            // Previsualizar la imagen seleccionada
            ImageIcon icon = new ImageIcon(imagenFile.getAbsolutePath());
            Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imagenLabel.setIcon(new ImageIcon(image));
        }
    }

    private void buscarCliente() {
        String identificacion = identificacionField.getText();
        if (identificacion.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un número de identificación.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
            String sql = "SELECT * FROM CLIENTES WHERE CLIIDENTIFICACION = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, identificacion);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                currentClientId = resultSet.getInt("CLICODIGO");
                nombre1Field.setText(resultSet.getString("CliNOMBRE1"));
                nombre2Field.setText(resultSet.getString("CLINOMBRE2"));
                apellido1Field.setText(resultSet.getString("CliAPELLIDO1"));
                apellido2Field.setText(resultSet.getString("CliAPELLIDO2"));
                direccionField.setText(resultSet.getString("CLIDIRECCION"));
                telefonoField.setText(resultSet.getString("CLITELEFONO"));
                celularField.setText(resultSet.getString("CLICELULAR"));
                emailField.setText(resultSet.getString("CLIEMAIL"));
                tipoComboBox.setSelectedItem(resultSet.getString("CLITIPO"));
                statusComboBox.setSelectedItem(resultSet.getString("CLISTATUS"));

                byte[] imagenBytes = resultSet.getBytes("CLIIMAGEN");
                if (imagenBytes != null) {
                    Image img = Toolkit.getDefaultToolkit().createImage(imagenBytes);
                    img = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    imagenLabel.setIcon(new ImageIcon(img));
                } else {
                    imagenLabel.setIcon(null);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar el cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarDatos(Integer clicodigo) {
        String nombre1 = nombre1Field.getText();
        String nombre2 = nombre2Field.getText();
        String apellido1 = apellido1Field.getText();
        String apellido2 = apellido2Field.getText();
        String identificacion = identificacionField.getText();
        String direccion = direccionField.getText();
        String telefono = telefonoField.getText();
        String celular = celularField.getText();
        String email = emailField.getText();
        String tipo = (String) tipoComboBox.getSelectedItem();
        String status = (String) statusComboBox.getSelectedItem();

        // Validación de campos requeridos
        if (nombre1.isEmpty() || apellido1.isEmpty() || identificacion.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || celular.isEmpty() || tipo == null || status == null) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos requeridos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación de CLITELEFONO y CLICELULAR
        if (!telefono.matches("\\d{9}")) {
            JOptionPane.showMessageDialog(null, "El teléfono debe tener exactamente 9 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!celular.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(null, "El celular debe tener exactamente 10 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validación de CLIEMAIL
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(null, "El email no tiene un formato válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Generar CLINOMBRE
        String clinombre = String.format("%s %s %s %s", nombre1, nombre2, apellido1, apellido2).trim().replaceAll(" +", " ");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
            String sql;
            PreparedStatement statement;

            if (clicodigo == null) {
                // Insertar nuevo registro
                sql = "INSERT INTO CLIENTES (CliNOMBRE1, CLINOMBRE2, CliAPELLIDO1, CliAPELLIDO2, CLINOMBRE, CLIIDENTIFICACION, CLIDIRECCION, CLITELEFONO, CLICELULAR, CLIEMAIL, CLITIPO, CLISTATUS, CLIIMAGEN) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                statement = conn.prepareStatement(sql);
            } else {
                // Actualizar registro existente
                sql = "UPDATE CLIENTES SET CliNOMBRE1 = ?, CLINOMBRE2 = ?, CliAPELLIDO1 = ?, CliAPELLIDO2 = ?, CLINOMBRE = ?, CLIIDENTIFICACION = ?, CLIDIRECCION = ?, CLITELEFONO = ?, CLICELULAR = ?, CLIEMAIL = ?, CLITIPO = ?, CLISTATUS = ?, CLIIMAGEN = ? WHERE CLICODIGO = ?";
                statement = conn.prepareStatement(sql);
                statement.setInt(14, clicodigo);
            }

            statement.setString(1, nombre1);
            statement.setString(2, nombre2);
            statement.setString(3, apellido1);
            statement.setString(4, apellido2);
            statement.setString(5, clinombre);
            statement.setString(6, identificacion);
            statement.setString(7, direccion);
            statement.setString(8, telefono);
            statement.setString(9, celular);
            statement.setString(10, email);
            statement.setString(11, tipo);
            statement.setString(12, status);

            if (imagenFile != null) {
                FileInputStream fis = new FileInputStream(imagenFile);
                statement.setBinaryStream(13, fis, (int) imagenFile.length());
            } else {
                statement.setNull(13, Types.BLOB);
            }

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Datos guardados exitosamente.");
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}

class FormularioProductos {
    private JPanel panel;
    private JTextField codigoField;
    private JTextField descripcionField;
    private JComboBox<String> unidadMedidaComboBox;
    private JTextField saldoInicialField;
    private JTextField ingresosField;
    private JTextField egresosField;
    private JTextField ajustesField;
    private JTextField saldoFinalField;
    private JTextField costoUMField;
    private JTextField precioUMField;
    private JComboBox<String> statusComboBox;
    private JLabel imagenLabel;
    private File imagenFile;
    private String currentProductCode = null; // Variable para almacenar el código del producto actual

    private final Dimension textFieldSize = new Dimension(200, 30);
    private final Font textFieldFont = new Font("Arial", Font.PLAIN, 14);

    public FormularioProductos() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(16, 2, 10, 10));
        formPanel.setBackground(new Color(240, 248, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Código:"));
        codigoField = new JTextField();
        codigoField.setPreferredSize(textFieldSize);
        codigoField.setFont(textFieldFont);
        formPanel.add(codigoField);

        JButton buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProducto();
            }
        });
        formPanel.add(buscarButton);

        formPanel.add(new JLabel("Descripción:"));
        descripcionField = new JTextField();
        descripcionField.setPreferredSize(textFieldSize);
        descripcionField.setFont(textFieldFont);
        formPanel.add(descripcionField);

        formPanel.add(new JLabel("Unidad de Medida:"));
        unidadMedidaComboBox = new JComboBox<>(new String[]{"Litros", "Galones", "Quintales", "Arrobas", "Libras"});
        unidadMedidaComboBox.setPreferredSize(textFieldSize);
        unidadMedidaComboBox.setFont(textFieldFont);
        formPanel.add(unidadMedidaComboBox);

        formPanel.add(new JLabel("Saldo Inicial:"));
        saldoInicialField = new JTextField();
        saldoInicialField.setPreferredSize(textFieldSize);
        saldoInicialField.setFont(textFieldFont);
        formPanel.add(saldoInicialField);

        formPanel.add(new JLabel("Ingresos:"));
        ingresosField = new JTextField();
        ingresosField.setPreferredSize(textFieldSize);
        ingresosField.setFont(textFieldFont);
        formPanel.add(ingresosField);

        formPanel.add(new JLabel("Egresos:"));
        egresosField = new JTextField();
        egresosField.setPreferredSize(textFieldSize);
        egresosField.setFont(textFieldFont);
        formPanel.add(egresosField);

        formPanel.add(new JLabel("Ajustes:"));
        ajustesField = new JTextField();
        ajustesField.setPreferredSize(textFieldSize);
        ajustesField.setFont(textFieldFont);
        formPanel.add(ajustesField);

        formPanel.add(new JLabel("Saldo Final:"));
        saldoFinalField = new JTextField();
        saldoFinalField.setPreferredSize(textFieldSize);
        saldoFinalField.setFont(textFieldFont);
        formPanel.add(saldoFinalField);

        formPanel.add(new JLabel("Costo UM:"));
        costoUMField = new JTextField();
        costoUMField.setPreferredSize(textFieldSize);
        costoUMField.setFont(textFieldFont);
        formPanel.add(costoUMField);

        formPanel.add(new JLabel("Precio UM:"));
        precioUMField = new JTextField();
        precioUMField.setPreferredSize(textFieldSize);
        precioUMField.setFont(textFieldFont);
        formPanel.add(precioUMField);

        formPanel.add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(new String[]{"ACT", "INA", "DES"});
        statusComboBox.setPreferredSize(textFieldSize);
        statusComboBox.setFont(textFieldFont);
        formPanel.add(statusComboBox);

        JButton cargarImagenButton = new JButton("Cargar Imagen");
        cargarImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarImagen();
            }
        });
        formPanel.add(cargarImagenButton);

        imagenLabel = new JLabel();
        formPanel.add(imagenLabel);

        JButton guardarButton = new JButton("Guardar");
        guardarButton.setBackground(new Color(34, 139, 34)); // Verde oscuro
        guardarButton.setForeground(Color.WHITE);
        guardarButton.setFont(new Font("Arial", Font.BOLD, 14));
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarDatos(null); // Para insertar un nuevo registro
            }
        });
        formPanel.add(guardarButton);

        JButton editarButton = new JButton("Editar");
        editarButton.setBackground(new Color(255, 140, 0)); // Naranja oscuro
        editarButton.setForeground(Color.WHITE);
        editarButton.setFont(new Font("Arial", Font.BOLD, 14));
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentProductCode != null) {
                    guardarDatos(currentProductCode); // Para editar el registro actual
                } else {
                    JOptionPane.showMessageDialog(panel, "Por favor, busque un producto para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        formPanel.add(editarButton);

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setBackground(new Color(178, 34, 34)); // Rojo oscuro
        cancelarButton.setForeground(Color.WHITE);
        cancelarButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        formPanel.add(cancelarButton);

        JButton verProductosButton = new JButton("Ver Productos");
        verProductosButton.setBackground(new Color(70, 130, 180)); // Azul oscuro
        verProductosButton.setForeground(Color.WHITE);
        verProductosButton.setFont(new Font("Arial", Font.BOLD, 14));
        verProductosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verProductos();
            }
        });
        formPanel.add(verProductosButton);

        panel.add(formPanel, BorderLayout.CENTER);
    }

    private void cargarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagenFile = fileChooser.getSelectedFile();
            imagenLabel.setText(imagenFile.getName());

            // Previsualizar la imagen seleccionada
            ImageIcon icon = new ImageIcon(imagenFile.getAbsolutePath());
            Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imagenLabel.setIcon(new ImageIcon(image));
        }
    }

    private void buscarProducto() {
        String codigo = codigoField.getText();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un código de producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
            String sql = "SELECT * FROM PRODUCTOS WHERE PROCODIGO = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, codigo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                currentProductCode = resultSet.getString("PROCODIGO");
                descripcionField.setText(resultSet.getString("PRODESCRIPCION"));
                unidadMedidaComboBox.setSelectedItem(resultSet.getString("PROUNIDADMEDIDA"));
                saldoInicialField.setText(resultSet.getString("PROSALDOINICIAL"));
                ingresosField.setText(resultSet.getString("PROINGRESOS"));
                egresosField.setText(resultSet.getString("PROEGRESOS"));
                ajustesField.setText(resultSet.getString("PROAJUSTES"));
                saldoFinalField.setText(resultSet.getString("PROSALDOFINAL"));
                costoUMField.setText(resultSet.getString("PROCOSTOUM"));
                precioUMField.setText(resultSet.getString("PROPRECIOUM"));
                statusComboBox.setSelectedItem(resultSet.getString("PROSTATUS"));

                byte[] imagenBytes = resultSet.getBytes("PROIMAGEN");
                if (imagenBytes != null) {
                    Image img = Toolkit.getDefaultToolkit().createImage(imagenBytes);
                    img = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    imagenLabel.setIcon(new ImageIcon(img));
                } else {
                    imagenLabel.setIcon(null);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarDatos(String procodigo) {
        String codigo = codigoField.getText();
        String descripcion = descripcionField.getText();
        String unidadMedida = (String) unidadMedidaComboBox.getSelectedItem();
        String saldoInicial = saldoInicialField.getText();
        String ingresos = ingresosField.getText();
        String egresos = egresosField.getText();
        String ajustes = ajustesField.getText();
        String saldoFinal = saldoFinalField.getText();
        String costoUM = costoUMField.getText();
        String precioUM = precioUMField.getText();
        String status = (String) statusComboBox.getSelectedItem();

        // Validación de campos requeridos
        if (codigo.isEmpty() || descripcion.isEmpty() || unidadMedida == null || saldoInicial.isEmpty() || ingresos.isEmpty() || egresos.isEmpty() || ajustes.isEmpty() || saldoFinal.isEmpty() || costoUM.isEmpty() || precioUM.isEmpty() || status == null) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos requeridos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
            String sql;
            PreparedStatement statement;

            if (procodigo == null) {
                // Insertar nuevo registro
                sql = "INSERT INTO PRODUCTOS (PROCODIGO, PRODESCRIPCION, PROUNIDADMEDIDA, PROSALDOINICIAL, PROINGRESOS, PROEGRESOS, PROAJUSTES, PROSALDOFINAL, PROCOSTOUM, PROPRECIOUM, PROSTATUS, PROIMAGEN) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                statement = conn.prepareStatement(sql);
            } else {
                // Actualizar registro existente
                sql = "UPDATE PRODUCTOS SET PRODESCRIPCION = ?, PROUNIDADMEDIDA = ?, PROSALDOINICIAL = ?, PROINGRESOS = ?, PROEGRESOS = ?, PROAJUSTES = ?, PROSALDOFINAL = ?, PROCOSTOUM = ?, PROPRECIOUM = ?, PROSTATUS = ?, PROIMAGEN = ? WHERE PROCODIGO = ?";
                statement = conn.prepareStatement(sql);
                statement.setString(12, procodigo);
            }

            statement.setString(1, codigo);
            statement.setString(2, descripcion);
            statement.setString(3, unidadMedida);
            statement.setString(4, saldoInicial);
            statement.setString(5, ingresos);
            statement.setString(6, egresos);
            statement.setString(7, ajustes);
            statement.setString(8, saldoFinal);
            statement.setString(9, costoUM);
            statement.setString(10, precioUM);
            statement.setString(11, status);

            if (imagenFile != null) {
                FileInputStream fis = new FileInputStream(imagenFile);
                statement.setBinaryStream(12, fis, (int) imagenFile.length());
            } else {
                statement.setNull(12, Types.BLOB);
            }

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Datos guardados exitosamente.");
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verProductos() {
        JFrame frame = new JFrame("Lista de Productos");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);

        JLabel titleLabel = new JLabel("LISTA DE PRODUCTOS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(70, 130, 180));  // Color azul
        titleLabel.setForeground(Color.WHITE);
        frame.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Código", "Descripción", "Unidad de Medida", "Saldo Inicial", "Ingresos", "Egresos", "Ajustes", "Saldo Final", "Costo UM", "Precio UM", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
            String sql = "SELECT * FROM PRODUCTOS";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getString("PROCODIGO"));
                row.add(resultSet.getString("PRODESCRIPCION"));
                row.add(resultSet.getString("PROUNIDADMEDIDA"));
                row.add(resultSet.getString("PROSALDOINICIAL"));
                row.add(resultSet.getString("PROINGRESOS"));
                row.add(resultSet.getString("PROEGRESOS"));
                row.add(resultSet.getString("PROAJUSTES"));
                row.add(resultSet.getString("PROSALDOFINAL"));
                row.add(resultSet.getString("PROCOSTOUM"));
                row.add(resultSet.getString("PROPRECIOUM"));
                row.add(resultSet.getString("PROSTATUS"));
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al recuperar los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        frame.setVisible(true);
    }

    public JPanel getPanel() {
        return panel;
    }
}

class FacturaApp {
    private JPanel panel;
    private JTextField idClienteField;
    private JTextField nombreClienteField;
    private JTextField fechaField;
    private JTextField numeroFacturaField;
    private JTextField codigoProductoField;
    private JTextField descripcionProductoField;
    private JComboBox<String> unidadMedidaComboBox;
    private JTextField precioUnitarioField;
    private JTextField cantidadField;
    private JTextField subtotalField;
    private JTable productosTable;
    private DefaultTableModel productosTableModel;
    private JLabel ivaLabel;
    private JLabel totalLabel;

    public FacturaApp() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(BorderFactory.createTitledBorder("Información de la Factura"));

        headerPanel.add(new JLabel("ID Cliente:"));
        idClienteField = new JTextField();
        headerPanel.add(idClienteField);

        JButton buscarClienteButton = new JButton("Buscar");
        buscarClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCliente();
            }
        });
        headerPanel.add(buscarClienteButton);

        headerPanel.add(new JLabel("Nombre Cliente:"));
        nombreClienteField = new JTextField();
        nombreClienteField.setEditable(false);
        headerPanel.add(nombreClienteField);

        headerPanel.add(new JLabel("Fecha:"));
        fechaField = new JTextField();
        fechaField.setText(java.time.LocalDate.now().toString());
        fechaField.setEditable(false);
        headerPanel.add(fechaField);

        headerPanel.add(new JLabel("Número de Factura:"));
        numeroFacturaField = new JTextField();
        numeroFacturaField.setEditable(false);
        headerPanel.add(numeroFacturaField);

        JPanel productPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        productPanel.setBackground(new Color(240, 248, 255));
        productPanel.setBorder(BorderFactory.createTitledBorder("Información del Producto"));

        productPanel.add(new JLabel("Código Producto:"));
        codigoProductoField = new JTextField();
        productPanel.add(codigoProductoField);

        JButton buscarProductoButton = new JButton("Buscar");
        buscarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProducto();
            }
        });
        productPanel.add(buscarProductoButton);

        productPanel.add(new JLabel("Descripción:"));
        descripcionProductoField = new JTextField();
        descripcionProductoField.setEditable(false);
        productPanel.add(descripcionProductoField);

        productPanel.add(new JLabel("Unidad de Medida:"));
        unidadMedidaComboBox = new JComboBox<>(new String[]{"Litros", "Galones", "Quintales", "Arrobas", "Libras"});
        unidadMedidaComboBox.setEnabled(false);
        productPanel.add(unidadMedidaComboBox);

        productPanel.add(new JLabel("Precio Unitario:"));
        precioUnitarioField = new JTextField();
        precioUnitarioField.setEditable(false);
        productPanel.add(precioUnitarioField);

        productPanel.add(new JLabel("Cantidad:"));
        cantidadField = new JTextField();
        productPanel.add(cantidadField);

        JButton agregarProductoButton = new JButton("Agregar");
        agregarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto();
            }
        });
        productPanel.add(agregarProductoButton);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(productPanel, BorderLayout.CENTER);

        productosTableModel = new DefaultTableModel(new String[]{"Código", "Descripción", "Unidad de Medida", "Precio Unitario", "Cantidad", "Subtotal"}, 0);
        productosTable = new JTable(productosTableModel);

        JScrollPane scrollPane = new JScrollPane(productosTable);
        panel.add(scrollPane, BorderLayout.EAST);

        JPanel footerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        footerPanel.setBackground(new Color(240, 248, 255));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        footerPanel.add(new JLabel("IVA:"));
        ivaLabel = new JLabel();
        footerPanel.add(ivaLabel);

        footerPanel.add(new JLabel("Total:"));
        totalLabel = new JLabel();
        footerPanel.add(totalLabel);

        JButton guardarFacturaButton = new JButton("Guardar Factura");
        guardarFacturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarFactura();
            }
        });
        footerPanel.add(guardarFacturaButton);

        panel.add(footerPanel, BorderLayout.SOUTH);
    }

    private void buscarCliente() {
        String idCliente = idClienteField.getText();
        if (idCliente.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID de cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
            String sql = "SELECT * FROM CLIENTES WHERE CLICODIGO = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, idCliente);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                nombreClienteField.setText(resultSet.getString("CLINOMBRE"));
            } else {
                JOptionPane.showMessageDialog(null, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar el cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarProducto() {
        String codigo = codigoProductoField.getText();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un código de producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
            String sql = "SELECT * FROM PRODUCTOS WHERE PROCODIGO = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, codigo);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                descripcionProductoField.setText(resultSet.getString("PRODESCRIPCION"));
                unidadMedidaComboBox.setSelectedItem(resultSet.getString("PROUNIDADMEDIDA"));
                precioUnitarioField.setText(resultSet.getString("PROPRECIOUM"));
            } else {
                JOptionPane.showMessageDialog(null, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al buscar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarProducto() {
        String codigo = codigoProductoField.getText();
        String descripcion = descripcionProductoField.getText();
        String unidadMedida = (String) unidadMedidaComboBox.getSelectedItem();
        String precioUnitarioStr = precioUnitarioField.getText();
        String cantidadStr = cantidadField.getText();

        if (codigo.isEmpty() || descripcion.isEmpty() || unidadMedida == null || precioUnitarioStr.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double precioUnitario = Double.parseDouble(precioUnitarioStr);
            int cantidad = Integer.parseInt(cantidadStr);
            double subtotal = precioUnitario * cantidad;

            productosTableModel.addRow(new Object[]{codigo, descripcion, unidadMedida, precioUnitario, cantidad, subtotal});
            actualizarTotales();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Precio unitario y cantidad deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTotales() {
        double total = 0;
        for (int i = 0; i < productosTableModel.getRowCount(); i++) {
            total += (double) productosTableModel.getValueAt(i, 5);
        }
        double iva = total * 0.12; // Suponiendo que el IVA es del 12%
        ivaLabel.setText(String.format("%.2f", iva));
        totalLabel.setText(String.format("%.2f", total + iva));
    }

    private void guardarFactura() {
        String idCliente = idClienteField.getText();
        String nombreCliente = nombreClienteField.getText();
        String fecha = fechaField.getText();
        String numeroFactura = numeroFacturaField.getText();
        double iva = Double.parseDouble(ivaLabel.getText());
        double total = Double.parseDouble(totalLabel.getText());

        if (idCliente.isEmpty() || nombreCliente.isEmpty() || fecha.isEmpty() || numeroFactura.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos de la factura.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
            String sql = "INSERT INTO FACTURAS (ID_CLIENTE, NOMBRE_CLIENTE, FECHA, NUMERO_FACTURA, IVA, TOTAL) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, idCliente);
            statement.setString(2, nombreCliente);
            statement.setString(3, fecha);
            statement.setString(4, numeroFactura);
            statement.setDouble(5, iva);
            statement.setDouble(6, total);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int facturaId = generatedKeys.getInt(1);
                    guardarDetallesFactura(conn, facturaId);
                    JOptionPane.showMessageDialog(null, "Factura guardada exitosamente.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar la factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarDetallesFactura(Connection conn, int facturaId) throws SQLException {
        String sql = "INSERT INTO DETALLES_FACTURA (FACTURA_ID, CODIGO_PRODUCTO, DESCRIPCION, UNIDAD_MEDIDA, PRECIO_UNITARIO, CANTIDAD, SUBTOTAL) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);

        for (int i = 0; i < productosTableModel.getRowCount(); i++) {
            String codigoProducto = (String) productosTableModel.getValueAt(i, 0);
            String descripcion = (String) productosTableModel.getValueAt(i, 1);
            String unidadMedida = (String) productosTableModel.getValueAt(i, 2);
            double precioUnitario = (double) productosTableModel.getValueAt(i, 3);
            int cantidad = (int) productosTableModel.getValueAt(i, 4);
            double subtotal = (double) productosTableModel.getValueAt(i, 5);

            statement.setInt(1, facturaId);
            statement.setString(2, codigoProducto);
            statement.setString(3, descripcion);
            statement.setString(4, unidadMedida);
            statement.setDouble(5, precioUnitario);
            statement.setInt(6, cantidad);
            statement.setDouble(7, subtotal);
            statement.addBatch();
        }

        statement.executeBatch();
    }

    public JPanel getPanel() {
        return panel;
    }
}

class AdministrarFacturasApp {
    private JPanel panel;
    private JTable facturasTable;
    private DefaultTableModel facturasTableModel;

    public AdministrarFacturasApp() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("LISTA DE FACTURAS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(70, 130, 180));  // Color azul
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.NORTH);

        facturasTableModel = new DefaultTableModel(new String[]{"ID", "Cliente", "Fecha", "Número de Factura", "IVA", "Total"}, 0);
        facturasTable = new JTable(facturasTableModel);

        JScrollPane scrollPane = new JScrollPane(facturasTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        cargarFacturas();
    }

    private void cargarFacturas() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
            String sql = "SELECT * FROM FACTURAS";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getInt("ID"));
                row.add(resultSet.getString("NOMBRE_CLIENTE"));
                row.add(resultSet.getString("FECHA"));
                row.add(resultSet.getString("NUMERO_FACTURA"));
                row.add(resultSet.getDouble("IVA"));
                row.add(resultSet.getDouble("TOTAL"));
                facturasTableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al recuperar los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
