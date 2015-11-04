package database.storage;

import java.util.Map;

import database.metadata.interfaces.IColumnDef;
import database.utils.Pair;

public class Result {

	private final byte[] buffer;
	private final int recordSize;
	private final Map<IColumnDef, Pair<Integer, Integer>> map;
	private int recordIndex;

	public Result(byte[] buffer, int recordSize, Map<IColumnDef, Pair<Integer, Integer>> map) {
		this.buffer = buffer;
		this.recordSize = recordSize;
		this.map = map;

	}

	public String getAsString(IColumnDef columnDef) {
		int offset = getOffset();

		Pair<Integer, Integer> pair = map.get(columnDef);

		String str = DataUtils.readString(buffer, offset + pair.getFst(), pair.getSnd());

		return str.trim();
	}

	public int getAsInteger(IColumnDef columnDef) {
		int offset = getOffset();

		Pair<Integer, Integer> pair = map.get(columnDef);

		return DataUtils.readInt(buffer, offset + pair.getFst());
	}

	private int getOffset() {
		return recordSize * recordIndex;
	}

	public void previous() {
		recordIndex--;
	}

	public void next() {
		recordIndex++;
	}

}
