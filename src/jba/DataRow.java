package jba;

import java.time.LocalDate;

//Class to hold required data for db actions
public	class DataRow {
		private String xRef;
		private String yRef;
		private LocalDate date;
		private int value;

		public String getxRef() {
			return xRef;
		}

		public void setxRef(String xRef) {
			this.xRef = xRef;
		}

		public String getyRef() {
			return yRef;
		}

		public void setyRef(String yRef) {
			this.yRef = yRef;
		}

		public LocalDate getDate() {
			return date;
		}

		public void setDate(LocalDate date) {
			this.date = date;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "DataRow [xRef=" + xRef + ", yRef=" + yRef + ", date=" + date + ", value=" + value + "]";
		}

	}

