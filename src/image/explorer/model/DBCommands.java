package image.explorer.model;



public class DBCommands {
	
	private DBConnect db;
	
	public DBCommands(DBConnect d) {
		this.db = d;
	}
	
	public boolean insert(String tableName, String[] columns, String[][] values) {
		boolean ret = false;
		String query = "";
		
		System.out.println("\n==================================================");
		System.out.println("Creating [INSERT] statement...");
		
		query = "INSERT INTO " + tableName + "\n(";
		query = columnsAppend(query, columns);
		query += ")\nVALUES\n('" + values[0][0];
		for(int j = 0; j < values.length; j++) {
			for(int i = 1; i < columns.length; i++) {
				query += "', '" + values[j][i];
			}
			if(j != (values.length - 1)) {
				query += "'),\n('" + values[j + 1][0];
			}
		}
		query += "');";
		
		ret = db.sqlExecute(query);
		
		System.out.println("==================================================");
		
		return ret;
	}
	
	public boolean update(String tableName, String[] setColumns, String[] setValues, String[] whereColumns, String[] whereValues, String[] whereBooleans) {
		boolean ret = false;
		String query = "";
		
		System.out.println("\n==================================================");
		System.out.println("Creating [UPDATE] statement...");
		
		query = "UPDATE " + tableName + "\nSET ";
		query += setColumns[0] + "='" + setValues[0];
		for(int i = 1; i < setColumns.length; i++) {
			query += "', " + setColumns[i] + "='" + setValues[i];
		}
		query += "'";
		if(whereColumns != null) {
			query = whereAppend(query, whereColumns, whereValues, whereBooleans);
		}
		query += ";";
		
		ret = db.sqlExecute(query);
		
		System.out.println("==================================================");
		
		return ret;
	}
	
	public boolean delete(String tableName, String[] whereColumns, String[] whereValues, String[] whereBooleans) {
		boolean ret = false;
		String query = "";
		
		System.out.println("\n==================================================");
		System.out.println("Creating [DELETE] statement...");
		
		query = "DELETE FROM " + tableName;
		query = whereAppend(query, whereColumns, whereValues, whereBooleans);
		query += ";";
		
		ret = db.sqlExecute(query);
		
		System.out.println("==================================================");
		
		return ret;
	}
	
	public String[][] select(String tableName, String[] selectColumns, String[] whereColumns, String[] whereValues, String[] whereBooleans) {
		String[][] ret = null;
		int count = count(tableName, whereColumns, whereValues, whereBooleans);
		String query = "";
		
		System.out.println("\n==================================================");
		System.out.println("Creating [SELECT] statement...");
		
		query = "SELECT ";
		query = columnsAppend(query, selectColumns);
		query += "\nFROM " + tableName;
		if(whereColumns != null) {
			query = whereAppend(query, whereColumns, whereValues, whereBooleans);
			
			// Query fix.
			query = query.replaceAll("WHERE", "AND");
		}
		query += ";";
		System.out.println("SELECT QUERY: " );
		System.out.println(query);
		ret = db.sqlSelect(count, query);
		
		System.out.println("==================================================");
		
		return ret;
	}
	
	public int count(String tableName, String[] whereColumns, String[] whereValues, String[] whereBooleans) {
		int ret = -1;
		String query = "";
		
		System.out.println("\n==================================================");
		System.out.println("Creating [COUNT] statement...");
		
		query = "SELECT Count(*)\nFROM " + tableName;
		if(whereColumns != null) {
			query = whereAppend(query, whereColumns, whereValues, whereBooleans);
			query = query.replaceAll("WHERE", "AND");
		}
		query += ";";
		
		ret = db.sqlCount(query);
		
		System.out.println("==================================================");
		
		return ret;
	}
	
	private String whereAppend(String query, String[] whereColumns, String[] whereValues, String[] whereBooleans) {
		int startParens = whereColumns.length - 1;
		
		query += "\nWHERE ";//(" + whereColumns[0] + "='" + whereValues[0];
		
		for(int i = 0; i < startParens; i++)
			query += "(";
		
		query +=  whereColumns[0] + "='" + whereValues[0];
		
		for(int i = 1; i < whereColumns.length; i++) {
			if(i == 1)
				query += "'\n";
			else
				query += "')\n";
			
			if(whereBooleans != null)
				query += whereBooleans[i-1] + " " + whereColumns[i] + "='" + whereValues[i];
			else
				query += whereColumns[i] + "='" + whereValues[i];
		}
		
		if(whereColumns.length == 1)
			query += "'";
		else
			query += "')";
		
		return query;
	}
	
	private String columnsAppend(String query, String[] columns) {
		query += columns[0];
		for(int i = 1; i < columns.length; i++) {
			query += ", " + columns[i];
		}
		
		return query;
	}
	
	public int getNextPID() {
		DBConnect db = new DBConnect();
		String statement = "Select Auto_increment\n" +
							"From information_schema.tables\n" +
							"WHERE table_name='tblFileData'\n" +
							"AND table_schema='dbImageExplorer'";
		int rv = db.sqlExecuteResult(statement);
		return rv;
	}
	
//	protected int backup() {
//		int processComplete = -1;
//		if(System.getProperty("os.name").contains("Windows"))
//			processComplete = db.cmdBackup();
//		else
//			processComplete = db.bashBackup();
//		return processComplete;
//	}
	
//	protected int restore() {
//		int processComplete = -1;
//		if(System.getProperty("os.name").contains("Windows"))
//			processComplete = db.cmdRestore();
//		else
//			processComplete = db.bashRestore();
//		return processComplete;
//	}
	
}
