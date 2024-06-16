import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;

public class FacturaApp {
    private JFrame frame;
    private JTextField idClienteField, nombreClienteField, numeroFacturaField, fechaField;
    private JTextField codigoProductoField, descripcionField, unidadMedidaField, precioUnitarioField, cantidadField;
    private JTable table, facturaTable;
    private DefaultTableModel tableModel, facturaTableModel;
    private JLabel subtotalLabel, totalLabel;
    private JComboBox<String> ivaComboBox;

    public FacturaApp() {
        frame = new JFrame("Generar Factura");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        headerPanel.add(new JLabel("ID Cliente:"));
        idClienteField = new JTextField();
        headerPanel.add(idClienteField);

        headerPanel.add(new JLabel("Nombre Cliente:"));
        nombreClienteField = new JTextField();
        headerPanel.add(nombreClienteField);

        headerPanel.add(new JLabel("Número de Factura:"));
        numeroFacturaField = new JTextField();
        headerPanel.add(numeroFacturaField);

        headerPanel.add(new JLabel("Fecha:"));
        fechaField = new JTextField();
        fechaField.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        fechaField.setEditable(false);
        headerPanel.add(fechaField);

        JButton buscarClienteButton = new JButton("Buscar Cliente");
        buscarClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCliente();
            }
        });
        headerPanel.add(buscarClienteButton);

        frame.add(headerPanel, BorderLayout.NORTH);

        JPanel productPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        productPanel.add(new JLabel("Código Producto:"));
        codigoProductoField = new JTextField();
        productPanel.add(codigoProductoField);

        productPanel.add(new JLabel("Descripción:"));
        descripcionField = new JTextField();
        descripcionField.setEditable(false);
        productPanel.add(descripcionField);

        productPanel.add(new JLabel("Unidad de Medida:"));
        unidadMedidaField = new JTextField();
        unidadMedidaField.setEditable(false);
        productPanel.add(unidadMedidaField);

        productPanel.add(new JLabel("Precio Unitario:"));
        precioUnitarioField = new JTextField();
        precioUnitarioField.setEditable(false);
        productPanel.add(precioUnitarioField);

        productPanel.add(new JLabel("Cantidad:"));
        cantidadField = new JTextField();
        productPanel.add(cantidadField);

        productPanel.add(new JLabel("IVA (%):"));
        ivaComboBox = new JComboBox<>(new String[]{"0", "8", "12", "15"});
        productPanel.add(ivaComboBox);

        JButton buscarProductoButton = new JButton("Buscar Producto");
        buscarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProducto();
            }
        });
        productPanel.add(buscarProductoButton);

        JButton agregarProductoButton = new JButton("Agregar Producto");
        agregarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto();
            }
        });
        productPanel.add(agregarProductoButton);

        frame.add(productPanel, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(new String[]{"Código", "Descripción", "Unidad de Medida", "Precio Unitario", "Cantidad", "IVA (%)", "Subtotal"}, 0);
        table = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(table);
        frame.add(tableScrollPane, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel productListLabel = new JLabel("Productos en la Factura:");
        rightPanel.add(productListLabel, BorderLayout.NORTH);

        JTable productTable = new JTable(tableModel);
        JScrollPane listScrollPane = new JScrollPane(productTable);
        rightPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new GridLayout(3, 1));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        subtotalLabel = new JLabel("Subtotal: $0.00");
        footerPanel.add(subtotalLabel);

        totalLabel = new JLabel("Total: $0.00");
        footerPanel.add(totalLabel);

        JButton guardarFacturaButton = new JButton("Guardar Factura");
        guardarFacturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarFactura();
            }
        });
        footerPanel.add(guardarFacturaButton);

        JButton administradorButton = new JButton("Administrador");
        administradorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarListaFacturas();
            }
        });
        footerPanel.add(administradorButton);

        frame.add(rightPanel, BorderLayout.EAST);
        frame.add(footerPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void buscarCliente() {
        String idCliente = idClienteField.getText();
        if (idCliente != null && !idCliente.trim().isEmpty()) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
                String sql = "SELECT CLINOMBRE FROM CLIENTES WHERE CLIIDENTIFICACION = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, idCliente);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    nombreClienteField.setText(resultSet.getString("CLINOMBRE"));
                } else {
                    JOptionPane.showMessageDialog(frame, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error al buscar el cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarProducto() {
        String codigo = codigoProductoField.getText();
        if (codigo != null && !codigo.trim().isEmpty()) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
                String sql = "SELECT PRODESCRIPCION, PROUNIDADMEDIDA, PROPRECIOUM FROM PRODUCTOS WHERE PROCODIGO = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, codigo);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    descripcionField.setText(resultSet.getString("PRODESCRIPCION"));
                    unidadMedidaField.setText(resultSet.getString("PROUNIDADMEDIDA"));
                    precioUnitarioField.setText(resultSet.getString("PROPRECIOUM"));
                } else {
                    JOptionPane.showMessageDialog(frame, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error al buscar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void agregarProducto() {
        String codigo = codigoProductoField.getText();
        String descripcion = descripcionField.getText();
        String unidadMedida = unidadMedidaField.getText();
        String precioUnitarioStr = precioUnitarioField.getText();
        String cantidadStr = cantidadField.getText();
        String ivaStr = (String) ivaComboBox.getSelectedItem();

        if (codigo.isEmpty() || descripcion.isEmpty() || unidadMedida.isEmpty() || precioUnitarioStr.isEmpty() || cantidadStr.isEmpty() || ivaStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Por favor, complete todos los campos del producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            double precioUnitario = Double.parseDouble(precioUnitarioStr);
            int iva = Integer.parseInt(ivaStr);
            double subtotal = cantidad * precioUnitario * (1 + iva / 100.0);
            tableModel.addRow(new Object[]{codigo, descripcion, unidadMedida, precioUnitario, cantidad, iva, subtotal});
            actualizarTotales();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Por favor, ingrese valores numéricos válidos para el precio unitario y la cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTotales() {
        double subtotal = 0.0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            subtotal += (double) tableModel.getValueAt(i, 6); // Subtotal
        }
        subtotalLabel.setText("Subtotal: $" + String.format("%.2f", subtotal));
        totalLabel.setText("Total: $" + String.format("%.2f", subtotal));
    }

    private void guardarFactura() {
        String idCliente = idClienteField.getText();
        String nombreCliente = nombreClienteField.getText();
        String numeroFactura = numeroFacturaField.getText();
        String fecha = fechaField.getText();
        double subtotal = 0.0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            subtotal += (double) tableModel.getValueAt(i, 6); // Subtotal
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
            String sqlFactura = "INSERT INTO FACTURAS (FACNUMERO, CLICODIGO, FACFECHA, FACSUBTOTAL, FACTOTAL) VALUES (?, (SELECT CLICODIGO FROM CLIENTES WHERE CLIIDENTIFICACION = ?), ?, ?, ?)";
            PreparedStatement statementFactura = conn.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS);
            statementFactura.setString(1, numeroFactura);
            statementFactura.setString(2, idCliente);
            statementFactura.setString(3, fecha);
            statementFactura.setDouble(4, subtotal);
            statementFactura.setDouble(5, subtotal);
            statementFactura.executeUpdate();

            ResultSet generatedKeys = statementFactura.getGeneratedKeys();
            if (generatedKeys.next()) {
                int facturaCodigo = generatedKeys.getInt(1);

                String sqlDetalle = "INSERT INTO DETALLES_FACTURA (FACTURACODIGO, PROCODIGO, CANTIDAD, PRECIOUNITARIO, SUBTOTAL) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statementDetalle = conn.prepareStatement(sqlDetalle);

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String codigoProducto = (String) tableModel.getValueAt(i, 0);
                    int cantidad = (int) tableModel.getValueAt(i, 4);
                    double precioUnitario = (double) tableModel.getValueAt(i, 3);
                    double subtotalProducto = (double) tableModel.getValueAt(i, 6);

                    statementDetalle.setInt(1, facturaCodigo);
                    statementDetalle.setString(2, codigoProducto);
                    statementDetalle.setInt(3, cantidad);
                    statementDetalle.setDouble(4, precioUnitario);
                    statementDetalle.setDouble(5, subtotalProducto);
                    statementDetalle.executeUpdate();
                }
            }

            JOptionPane.showMessageDialog(frame, "Factura guardada exitosamente.");
            limpiarFormulario();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al guardar la factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        idClienteField.setText("");
        nombreClienteField.setText("");
        numeroFacturaField.setText("");
        fechaField.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        codigoProductoField.setText("");
        descripcionField.setText("");
        unidadMedidaField.setText("");
        precioUnitarioField.setText("");
        cantidadField.setText("");
        tableModel.setRowCount(0);
        subtotalLabel.setText("Subtotal: $0.00");
        totalLabel.setText("Total: $0.00");
    }

    private void mostrarListaFacturas() {
        JFrame listaFrame = new JFrame("Lista de Facturas");
        listaFrame.setSize(800, 400);
        listaFrame.setLayout(new BorderLayout());

        facturaTableModel = new DefaultTableModel(new String[]{"ID", "Cliente", "Fecha", "Número de Factura", "Subtotal", "Total", "Acciones"}, 0);
        facturaTable = new JTable(facturaTableModel);

        cargarFacturas();

        TableColumn accionesColumn = facturaTable.getColumn("Acciones");
        accionesColumn.setCellRenderer(new ButtonRenderer());
        accionesColumn.setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(facturaTable);
        listaFrame.add(scrollPane, BorderLayout.CENTER);

        listaFrame.setVisible(true);
    }

    private void cargarFacturas() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
            String sql = "SELECT FACTURACODIGO, (SELECT CLINOMBRE FROM CLIENTES WHERE CLIENTES.CLICODIGO = FACTURAS.CLICODIGO) AS Cliente, FACFECHA, FACNUMERO, FACSUBTOTAL, FACTOTAL FROM FACTURAS";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                facturaTableModel.addRow(new Object[]{
                        resultSet.getInt("FACTURACODIGO"),
                        resultSet.getString("Cliente"),
                        resultSet.getDate("FACFECHA"),
                        resultSet.getString("FACNUMERO"),
                        resultSet.getDouble("FACSUBTOTAL"),
                        resultSet.getDouble("FACTOTAL"),
                        "Acciones"
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al cargar las facturas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton duplicateButton;
        private JButton cancelButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            duplicateButton = new JButton("Duplicar");
            duplicateButton.setBackground(Color.ORANGE);
            duplicateButton.setForeground(Color.WHITE);
            add(duplicateButton);

            cancelButton = new JButton("Anular");
            cancelButton.setBackground(Color.RED);
            cancelButton.setForeground(Color.WHITE);
            add(cancelButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton duplicateButton;
        private JButton cancelButton;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            duplicateButton = new JButton("Duplicar");
            duplicateButton.setBackground(Color.ORANGE);
            duplicateButton.setForeground(Color.WHITE);
            panel.add(duplicateButton);

            cancelButton = new JButton("Anular");
            cancelButton.setBackground(Color.RED);
            cancelButton.setForeground(Color.WHITE);
            panel.add(cancelButton);

            duplicateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    duplicarFactura(selectedRow);
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    anularFactura(selectedRow);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.selectedRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    private void duplicarFactura(int row) {
        int facturaCodigo = (int) facturaTableModel.getValueAt(row, 0);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
            String sqlFactura = "INSERT INTO FACTURAS (FACNUMERO, CLICODIGO, FACFECHA, FACSUBTOTAL, FACTOTAL) SELECT FACNUMERO, CLICODIGO, FACFECHA, FACSUBTOTAL, FACTOTAL FROM FACTURAS WHERE FACTURACODIGO = ?";
            PreparedStatement statementFactura = conn.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS);
            statementFactura.setInt(1, facturaCodigo);
            statementFactura.executeUpdate();

            ResultSet generatedKeys = statementFactura.getGeneratedKeys();
            if (generatedKeys.next()) {
                int nuevaFacturaCodigo = generatedKeys.getInt(1);

                String sqlDetalle = "INSERT INTO DETALLES_FACTURA (FACTURACODIGO, PROCODIGO, CANTIDAD, PRECIOUNITARIO, SUBTOTAL) SELECT ?, PROCODIGO, CANTIDAD, PRECIOUNITARIO, SUBTOTAL FROM DETALLES_FACTURA WHERE FACTURACODIGO = ?";
                PreparedStatement statementDetalle = conn.prepareStatement(sqlDetalle);
                statementDetalle.setInt(1, nuevaFacturaCodigo);
                statementDetalle.setInt(2, facturaCodigo);
                statementDetalle.executeUpdate();
            }

            JOptionPane.showMessageDialog(frame, "Factura duplicada exitosamente.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al duplicar la factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void anularFactura(int row) {
        int facturaCodigo = (int) facturaTableModel.getValueAt(row, 0);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prueba", "root", "Puce1324")) {
            String sqlAnular = "UPDATE FACTURAS SET FACSTATUS = 'ANULADA' WHERE FACTURACODIGO = ?";
            PreparedStatement statementAnular = conn.prepareStatement(sqlAnular);
            statementAnular.setInt(1, facturaCodigo);
            statementAnular.executeUpdate();

            // Restore product quantities
            String sqlRestore = "UPDATE PRODUCTOS p JOIN DETALLES_FACTURA d ON p.PROCODIGO = d.PROCODIGO SET p.PROSTOCK = p.PROSTOCK + d.CANTIDAD WHERE d.FACTURACODIGO = ?";
            PreparedStatement statementRestore = conn.prepareStatement(sqlRestore);
            statementRestore.setInt(1, facturaCodigo);
            statementRestore.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Factura anulada exitosamente.");
            facturaTableModel.setValueAt("ANULADA", row, 1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al anular la factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FacturaApp();
            }
        });
    }
}
