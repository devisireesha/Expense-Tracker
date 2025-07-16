package com.expensetracker.dao;

import com.expensetracker.model.PaymentMode;
import com.expensetracker.util.Constants;
import com.expensetracker.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PaymentModeDAO {
	public static final Logger LOGGER = LogManager.getLogger(PaymentModeDAO.class);
	
	
	public PaymentMode getPaymentModeByID(int paymentModeID) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection();
			stmt = conn.prepareStatement(Constants.PaymentModeQuery.GET_PAYMENTMODE_BY_ID);
			stmt.setInt(1, paymentModeID);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return new PaymentMode(rs.getInt(Constants.ModeOfPaymentTable.PAYMENTID), rs.getString(Constants.ModeOfPaymentTable.PAYMENTNAME));
			}
			stmt.close();
		} catch (SQLException e) {
			DBConnection.handleSqlException(Constants.ClassMethod.M11, e, LOGGER);
		}catch(Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M45, e.getMessage());
		}finally {
			closeResources(conn, stmt, rs);
		}
		return null;
	}

	public List<PaymentMode> getAllPaymentModes() {
		int flag = 0;
		List<PaymentMode> paymentModes = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(Constants.PaymentModeQuery.GET_ALL_PAYMENTMODES);
			while (rs.next()) {
				paymentModes.add(new PaymentMode(rs.getInt(Constants.ModeOfPaymentTable.PAYMENTID), rs.getString(Constants.ModeOfPaymentTable.PAYMENTNAME)

				));
				flag = 1;
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			DBConnection.handleSqlException(Constants.ClassMethod.M10, e, LOGGER);
		}catch(Exception e) {
            LOGGER.error(Constants.LogMessage.Error.M44, e.getMessage());
		}finally {
			closeResources(conn, stmt, rs);
		}
		return paymentModes;
	}
	
	private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			LOGGER.error(Constants.LogMessage.Error.M20, e);
		}
	}
	
	private void closeResources(Connection conn, Statement stmt,ResultSet rs) {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			LOGGER.error(Constants.LogMessage.Error.M20, e);
		}
	}
}
