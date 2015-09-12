package database.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JTextArea;
import javax.swing.border.AbstractBorder;

public class NumberedBorder extends AbstractBorder {

	private static final long serialVersionUID = -5089118025935944759L;

	private static int lineHeight;
	private final int characterHeight = 8;
	private final int characterWidth = 7;
	private final Color myColor;

	NumberedBorder() {
		myColor = new Color(164, 164, 164);
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		final JTextArea textArea = (JTextArea) c;
		final Font font = textArea.getFont();
		final FontMetrics metrics = g.getFontMetrics(font);
		lineHeight = metrics.getHeight();

		final Color oldColor = g.getColor();
		g.setColor(myColor);

		final double r = (double) height / (double) lineHeight;
		final int rows = (int) (r + 0.5);
		String str = String.valueOf(rows);
		final int lineLeft = calculateLeft(height) + 10;

		int px = 0;
		int py = 0;
		int lenght = 0;

		final int visibleLines = textArea.getHeight() / lineHeight;
		for (int i = 0; i < visibleLines; i++) {

			str = String.valueOf(i + 1);
			lenght = str.length();

			py = lineHeight * i + 14;
			px = lineLeft - (characterWidth * lenght) - 2;

			g.drawString(str, px, py);
		}

		g.drawLine(lineLeft, 0, lineLeft, height);

		g.setColor(oldColor);
	}

	@Override
	public Insets getBorderInsets(Component c) {
		final int left = calculateLeft(c.getHeight()) + 13;
		return new Insets(1, left, 1, 1);
	}

	private int calculateLeft(int height) {
		final double r = (double) height / (double) lineHeight;
		final int rows = (int) (r + 0.5);
		final String str = String.valueOf(rows);
		final int lenght = str.length();
		return characterHeight * lenght;
	}
}
