package database.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import database.gals.LexicalError;
import database.gals.Lexico;
import database.gals.SemanticError;
import database.gals.Semantico;
import database.gals.Sintatico;
import database.gals.SyntaticError;
import database.manager.DatabaseManager;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;

public class SGBDView extends JFrame implements ActionListener {

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final Exception e) {
            System.err.println("Problemas com o look and feel");
            e.printStackTrace();
        }
    }

    private static final long serialVersionUID = 1L;
    private final JSplitPane splitVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private final JSplitPane splitHorizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    private final JTree jTree = new JTree(new DefaultTreeModel(new DefaultMutableTreeNode("Databases")));
    private final DefaultTreeModel treeModel;
    private final JMenuBar menuBar = new JMenuBar();
    private final JToolBar toolBar = new JToolBar();
    private final JButton btnExecutar = new JButton();
    private final JButton btnImportar = new JButton();
    private final JButton btnExportar = new JButton();
    private final JButton btnLimpar = new JButton();
    private final Result result = new Result();
    private JTextArea txtSql;

    public SGBDView() {
        addDadosFakes(); // TODO apagar

        final JPanel panelBotton = new JPanel(new BorderLayout());
        final JScrollPane spBottom = new JScrollPane(new JTable(new ResultTableModel(result)));
        panelBotton.add(spBottom, BorderLayout.CENTER);
        panelBotton.add(toolBar, BorderLayout.SOUTH);
        final JPanel painelCentroTop = new JPanel(new BorderLayout());
        txtSql = new JTextArea();
        txtSql.setBorder(new NumberedBorder());
        final JScrollPane spTop = new JScrollPane(txtSql);
        final JPanel principal = new JPanel(new BorderLayout());
        treeModel = (DefaultTreeModel) jTree.getModel();

        painelCentroTop.add(spTop, BorderLayout.CENTER);
        final JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnExecutar.setToolTipText("F5 - Executar o comando SQL");

        // TODO: Teste
        btnExecutar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Lexico lexico = new Lexico(txtSql.getText());
                Sintatico sintatico = new Sintatico();
                try {
                    sintatico.parse(lexico, new Semantico());
                } catch (LexicalError e1) {
                    e1.printStackTrace();
                } catch (SyntaticError e1) {
                    e1.printStackTrace();
                } catch (SemanticError e1) {
                    e1.printStackTrace();
                }
            }
        });

        btnImportar.setToolTipText("F7 - Importar comandos SQL de um arquivo");
        btnExportar.setToolTipText("F8 - Exportar comandos SQL para um arquivo");
        btnLimpar.setToolTipText("F9 - Limpar");
        btnExecutar.setIcon(new ImageIcon("src/main/java/img/executar.png"));// TODO corrigir
        btnImportar.setIcon(new ImageIcon("src/main/java/img/importar.png"));
        btnExportar.setIcon(new ImageIcon("src/main/java/img/exportar.png"));
        btnLimpar.setIcon(new ImageIcon("src/main/java/img/limpar.png"));
        btnExecutar.setFocusable(false);
        btnImportar.setFocusable(false);
        btnExportar.setFocusable(false);
        btnLimpar.setFocusable(false);
        painelBotoes.add(btnExecutar);
        painelBotoes.add(btnImportar);
        painelBotoes.add(btnExportar);
        painelBotoes.add(btnLimpar);
        painelCentroTop.add(painelBotoes, BorderLayout.NORTH);
        splitVertical.setTopComponent(painelCentroTop);
        splitVertical.setBottomComponent(panelBotton);
        splitHorizontal.setLeftComponent(jTree);
        splitHorizontal.setRightComponent(splitVertical);

        final JLabel txtInfo = new JLabel("Tempo total: 125             1024 linha(s)");
        txtInfo.setIcon(new ImageIcon("src/main/java/img/tempoTotal.png")); // TODO trocar para pegar do resource as stream
        toolBar.add(txtInfo);// TODO fazer update ser um método
        final JLabel lblEquipe = new JLabel("SGBD DANGARI");
        lblEquipe.setBorder(new LineBorder(Color.BLACK));
        lblEquipe.setHorizontalAlignment(SwingConstants.CENTER);
        lblEquipe.setFont(new Font("tahoma", Font.BOLD, 18));
        final JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.add(lblEquipe, BorderLayout.CENTER);

        menuBar.add(painelCentro);

        refreshDatabaseTree();

        principal.add(menuBar, BorderLayout.NORTH);
        principal.add(splitHorizontal, BorderLayout.CENTER);

        getContentPane().add(principal, BorderLayout.CENTER);

        addListenerButton(getContentPane());

        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        splitVertical.setDividerLocation(screenSize.height - 350);
        splitHorizontal.setDividerLocation(screenSize.width / 6);
        setVisible(true);

    }

    /**
     * 
     */
    private void refreshDatabaseTree() {
        // Descomente as linhas abaixo para inserir um database de teste em sua máquina
        //        File databaseFile = FileManager.createDatabase("Database Usuario-Colaborador");
        //        File usuarioTable = FileManager.createTable("Database Usuario-Colaborador", "Usuario");
        //        File colaboradorTable = FileManager.createTable("Database Usuario-Colaborador", "Colaborador");
        //
        //        ITableDef usuario = new TableDef("Usuario", new IColumnDef[] { new ColumnDef("codigo", DataType.INTEGER, 0), new ColumnDef("nome", DataType.VARCHAR, 100) });
        //        ITableDef colaborador = new TableDef("Colaborador", new IColumnDef[] { new ColumnDef("codigo", DataType.INTEGER, 0), new ColumnDef("codigoUsuario", DataType.INTEGER, 0), new ColumnDef("cargo", DataType.VARCHAR, 100) });
        //        DefStorage.setTableDef(usuarioTable, usuario);
        //        DefStorage.setTableDef(colaboradorTable, colaborador);

        ((DefaultMutableTreeNode) treeModel.getRoot()).removeAllChildren();
        Map<String, IDatabaseDef> databases = DatabaseManager.INSTANCE.getDatabases();
        for (Entry<String, IDatabaseDef> entry : databases.entrySet()) {
            DefaultMutableTreeNode databaseNode = addElementOnRoot(entry.getKey(), true);
            for (Entry<String, ITableDef> table : entry.getValue().getTables().entrySet()) {
                DefaultMutableTreeNode tableNode = addElementOnTree(table.getKey(), databaseNode);
                for (IColumnDef column : table.getValue().getColumns()) {
                    addElementOnTree(column.getName(), tableNode);
                }
            }
        }
    }

    private void addDadosFakes() {
        final List<String> ids = new ArrayList<String>();
        ids.add("1");
        ids.add("2");
        ids.add("3");
        ids.add("4");
        ids.add("5");
        ids.add("6");
        ids.add("7");
        ids.add("1");
        ids.add("2");
        ids.add("3");
        ids.add("4");
        ids.add("5");
        ids.add("6");
        ids.add("7");
        ids.add("1");
        ids.add("2");
        ids.add("3");
        ids.add("4");
        ids.add("5");
        ids.add("6");
        ids.add("7");
        final List<String> nomes = new ArrayList<String>();
        nomes.add("joao");
        nomes.add("pedro");
        nomes.add("daniel");
        nomes.add("gabriel");
        nomes.add("ricardo");
        nomes.add("jose");
        nomes.add("damiao");
        nomes.add("joao");
        nomes.add("pedro");
        nomes.add("daniel");
        nomes.add("gabriel");
        nomes.add("ricardo");
        nomes.add("jose");
        nomes.add("damiao");
        nomes.add("joao");
        nomes.add("pedro");
        nomes.add("daniel");
        nomes.add("gabriel");
        nomes.add("ricardo");
        nomes.add("jose");
        nomes.add("damiao");
        result.addLine(new Column("id"), ids);
        result.addLine(new Column("nome"), nomes);
    }

    /**
     * Adiciona o action listener desta classe para todos os componentes que são botões e menus.
     */
    private void addListenerButton(Container c) {
        final Component[] components = c.getComponents();
        for (final Component component : components) {
            if (component instanceof Container) {
                addListenerButton((Container) component);
            } else if (component instanceof JButton) {
                ((JButton) component).addActionListener(this);
            }
        }
    }

    public static void main(String[] args) {
        new SGBDView();
    }

    public DefaultMutableTreeNode addElementOnRoot(String elemento, boolean permiteArquivosFilhos) {
        final DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        return addElementOnTree(elemento, root);
    }

    public DefaultMutableTreeNode addElementOnTree(String nodeName, DefaultMutableTreeNode node) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(nodeName, true);
        node.add(newNode);
        return newNode;
    }

    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    }

}
