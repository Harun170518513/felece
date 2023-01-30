package com.ra.busBooking.DTO;

/**
 * @author Ramanpreet
 *
 */
public class BookingsDTO {

	private int id;
	 private String filterDate;
		

		private String fromDestination;
		private String toDestination;

		
		private String BusName;
		private Double price;
		private String time;
		private Double totalCalculated ;
		private int noOfPersons ;
		private String tripStatus;

		public String getFilterDate() {
			return filterDate;
		}

		public void setFilterDate(String filterDate) {
			this.filterDate = filterDate;
		}

		public String getToDestination() {
			return toDestination;
		}

		public void setToDestination(String toDestination) {
			this.toDestination = toDestination;
		}

		public String getFromDestination() {
			return fromDestination;
		}

		public void setFromDestination(String fromDestination) {
			this.fromDestination = fromDestination;
		}

		public Double getPrice() {
			return price;
		}

		public void setPrice(Double price) {
			this.price = price;
		}

		public String getBusName() {
			return BusName;
		}

		public void setBusName(String busName) {
			BusName = busName;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public int getNoOfPersons() {
			return noOfPersons;
		}

		public void setNoOfPersons(int noOfPersons) {
			this.noOfPersons = noOfPersons;
		}

		public Double getTotalCalculated() {
			return totalCalculated;
		}

		public void setTotalCalculated(Double totalCalculated) {
			this.totalCalculated = totalCalculated;
		}

		public String getTripStatus() {
			return tripStatus;
		}

		public void setTripStatus(String tripStatus) {
			this.tripStatus = tripStatus;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		
		
		
		
}
