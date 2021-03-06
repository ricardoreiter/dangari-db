package database.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
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
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import database.command.CommandResult;
import database.command.ICommandExecutor;
import database.gals.LexicalError;
import database.gals.Lexico;
import database.gals.SemanticError;
import database.gals.Semantico;
import database.gals.Sintatico;
import database.gals.SyntaticError;
import database.manager.DatabaseManager;
import database.manager.DatabaseManager.DatabaseManagerListener;
import database.metadata.DataType;
import database.metadata.interfaces.IColumnDef;
import database.metadata.interfaces.IDatabaseDef;
import database.metadata.interfaces.ITableDef;

public class SGBDView extends JFrame {

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
	private final JButton btnNextCommand = new JButton();
	private final JButton btnPriorCommand = new JButton();
	private final JTable resultTable = new JTable();
	private JTextArea txtSql;
	private LinkedList<String> commandList = new LinkedList<>();
	private byte actualCommandIndex = 0;
	private static final byte COMMAND_LIST_MAX_SIZE = 50;

	public SGBDView() {
		final JPanel panelBotton = new JPanel(new BorderLayout());
		final JScrollPane spBottom = new JScrollPane(resultTable);
		panelBotton.add(spBottom, BorderLayout.CENTER);
		panelBotton.add(toolBar, BorderLayout.SOUTH);
		final JPanel painelCentroTop = new JPanel(new BorderLayout());
		txtSql = new JTextArea();
		txtSql.setBorder(new NumberedBorder());
		final JScrollPane spTop = new JScrollPane(txtSql);
		final JPanel principal = new JPanel(new BorderLayout());
		treeModel = (DefaultTreeModel) jTree.getModel();
		jTree.setCellRenderer(new DatabaseTreeCustomRenderer());

		painelCentroTop.add(spTop, BorderLayout.CENTER);
		final JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));

		AbstractAction executeAction = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateCommandList();

				long initialTime = System.currentTimeMillis();

				Lexico lexico = new Lexico(txtSql.getText());
				Sintatico sintatico = new Sintatico();
				Semantico semanticAnalyser = new Semantico();
				CommandResult commandResult = new CommandResult();
				int linesAffected = 0;
				try {
					sintatico.parse(lexico, semanticAnalyser);
					for (ICommandExecutor executor : semanticAnalyser.getExecutor()) {
						commandResult = executor.execute();
						refreshResultConsole(commandResult);
						linesAffected += commandResult.getValues().entrySet().iterator().next().getValue().size();
					}
				} catch (LexicalError | SyntaticError | SemanticError e1) {
					commandResult = new CommandResult();
					commandResult.addColumn("Erro");
					commandResult.addValue("Erro", e1.getMessage());
					refreshResultConsole(commandResult);
				}
				long timeTook = System.currentTimeMillis() - initialTime;
				updateStatusBar(timeTook, linesAffected);
			}

		};

		AbstractAction nextCommandAction = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				nextCommand();
			}
		};

		AbstractAction priorCommandAction = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				priorCommand();
			}
		};

		btnNextCommand.addActionListener(nextCommandAction);
		btnPriorCommand.addActionListener(priorCommandAction);
		btnExecutar.addActionListener(executeAction);

		btnExecutar.setToolTipText("F5 Executar o comando SQL");
		btnImportar.setToolTipText("F7 Importar comandos SQL de um arquivo");
		btnExportar.setToolTipText("F8 Exportar comandos SQL para um arquivo");
		btnLimpar.setToolTipText("F9 Limpar");
		btnNextCommand.setToolTipText("Alt + > Avança para o próximo comando");
		btnPriorCommand.setToolTipText("Alt + < Retorna para o comando anterior");
		btnExecutar.setIcon(new ImageIcon("src/main/java/img/run.png"));// TODO
																					// corrigir
		btnImportar.setIcon(new ImageIcon("src/main/java/img/importar.png"));
		btnExportar.setIcon(new ImageIcon("src/main/java/img/exportar.png"));
		btnLimpar.setIcon(new ImageIcon("src/main/java/img/limpar.png"));
		btnNextCommand.setIcon(new ImageIcon("src/main/java/img/next.png"));
		btnPriorCommand.setIcon(new ImageIcon("src/main/java/img/previous.png"));

		btnExecutar.setFocusable(false);
		btnImportar.setFocusable(false);
		btnExportar.setFocusable(false);
		btnLimpar.setFocusable(false);

		btnExecutar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "F5");
		btnExecutar.getActionMap().put("F5", executeAction);
		btnNextCommand.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_DOWN_MASK), "ALT+RIGHT");
		btnNextCommand.getActionMap().put("ALT+RIGHT", nextCommandAction);
		btnPriorCommand.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_DOWN_MASK), "ALT+LEFT");
		btnPriorCommand.getActionMap().put("ALT+LEFT", priorCommandAction);

		painelBotoes.add(btnExecutar);
		painelBotoes.add(btnPriorCommand);
		painelBotoes.add(btnNextCommand);
		painelBotoes.add(btnImportar);
		painelBotoes.add(btnExportar);
		painelBotoes.add(btnLimpar);
		painelCentroTop.add(painelBotoes, BorderLayout.NORTH);
		splitVertical.setTopComponent(painelCentroTop);
		splitVertical.setBottomComponent(panelBotton);
		splitHorizontal.setLeftComponent(jTree);
		splitHorizontal.setRightComponent(splitVertical);

		updateStatusBar(0, 0);
		final JLabel lblEquipe = new JLabel("SGBD DANGARI");
		lblEquipe.setBorder(new LineBorder(Color.BLACK));
		lblEquipe.setHorizontalAlignment(SwingConstants.CENTER);
		lblEquipe.setFont(new Font("tahoma", Font.BOLD, 18));
		final JPanel painelCentro = new JPanel(new BorderLayout());
		painelCentro.add(lblEquipe, BorderLayout.CENTER);

		menuBar.add(painelCentro);

		refreshDatabaseTree();

		DatabaseManager.INSTANCE.setListener(new DatabaseManagerListener() {

			@Override
			public void onRefreshDatabases() {
				refreshDatabaseTree();

			}
		});

		principal.add(menuBar, BorderLayout.NORTH);
		principal.add(splitHorizontal, BorderLayout.CENTER);

		getContentPane().add(principal, BorderLayout.CENTER);

		setMinimumSize(new Dimension(800, 600));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		splitVertical.setDividerLocation(screenSize.height - 350);
		splitHorizontal.setDividerLocation(screenSize.width / 6);
		setVisible(true);

	}

	private void updateCommandList() {
		if (commandList.isEmpty() || !commandList.getLast().equals(txtSql.getText())) {
			commandList.addLast(txtSql.getText());
			if (commandList.size() > COMMAND_LIST_MAX_SIZE) {
				commandList.removeFirst();
			}
			actualCommandIndex = (byte) (commandList.size() - 1);
		}
	}

	private void nextCommand() {
		if (!commandList.isEmpty()) {
			if (actualCommandIndex < commandList.size() - 1) {
				actualCommandIndex++;
				txtSql.setText(commandList.get(actualCommandIndex));
			}
		}
	}

	private void priorCommand() {
		if (!commandList.isEmpty()) {
			if (actualCommandIndex > 0) {
				actualCommandIndex--;
				txtSql.setText(commandList.get(actualCommandIndex));
			}
		}
	}

	private void updateStatusBar(long timeTook, int lines) {
		final JLabel txtInfo = new JLabel(String.format("Tempo total: %sms             %s linha(s)", timeTook, lines));
		txtInfo.setIcon(new ImageIcon("src/main/java/img/tempoTotal.png")); // TODO
																			// trocar
																			// para
																			// pegar
																			// do
																			// resource
																			// as
																			// stream
		toolBar.removeAll();
		toolBar.add(txtInfo);// TODO fazer update ser um método
		toolBar.validate();
	}

	/**
	 * 
	 */
	private void refreshDatabaseTree() {
		((DefaultMutableTreeNode) treeModel.getRoot()).removeAllChildren();
		Map<String, IDatabaseDef> databases = DatabaseManager.INSTANCE.getDatabases();
		for (Entry<String, IDatabaseDef> entry : databases.entrySet()) {
			String treeText = entry.getKey();
			if (DatabaseManager.INSTANCE.getActualDatabase() == entry.getValue()) {
				treeText += " (ATIVO)";
			}
			DefaultMutableTreeNode databaseNode = addElementOnRoot(treeText, true);
			for (Entry<String, ITableDef> table : entry.getValue().getTables().entrySet()) {
				DefaultMutableTreeNode tableNode = addElementOnTree(table.getKey(), databaseNode);
				for (IColumnDef column : table.getValue().getColumns()) {
					addElementOnTree(column.getName() + " : " + column.getDataType()
							+ (column.getDataType() != DataType.INTEGER ? "(" + column.getCapacity() + ")" : "")
							+ (table.getValue().getIndex(column) != null ? " - INDEXED" : ""),
							tableNode);
				}
			}
		}
		treeModel.nodeStructureChanged((DefaultMutableTreeNode) treeModel.getRoot());
	}

	private void refreshResultConsole(CommandResult commandResult) {
		resultTable.setModel(new ResultTableModel(commandResult));
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

	private class DatabaseTreeCustomRenderer extends DefaultTreeCellRenderer {

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean exp, boolean leaf,
				int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			if (((String) node.getUserObject()).endsWith("(ATIVO)")) {
				setForeground(Color.GREEN);
			}
			return this;
		}
	}

}
