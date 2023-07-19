package edu.project.bookmyshow.util;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import edu.project.bookmyshow.dao.BookingDao;
import edu.project.bookmyshow.dao.ScreenDao;
import edu.project.bookmyshow.dao.ShowDao;
import edu.project.bookmyshow.dao.TicketDao;
import edu.project.bookmyshow.entity.Booking;
import edu.project.bookmyshow.entity.Screen;
import edu.project.bookmyshow.entity.Show;
import edu.project.bookmyshow.entity.Ticket;
import edu.project.bookmyshow.enums.BookingStatus;
import edu.project.bookmyshow.enums.ScreenAvailability;
import edu.project.bookmyshow.enums.Screenstatus;
import edu.project.bookmyshow.enums.ShowStatus;
import edu.project.bookmyshow.enums.TicketStatus;

@Component
public class ScheduledTasks {
	
	@Autowired
	private ShowDao showDao;
	@Autowired
	private ScreenDao screenDao;
	@Autowired
	private TicketDao ticketDao;
	@Autowired
	private BookingDao bookingDao;
	

	
	@Scheduled(cron = "0 * * * * *")
	public void setShowToOnGoing() {
		
		List<Show> shows = showDao.getShowsByTime(LocalDateTime.now(), ShowStatus.ACTIVE);
		if(shows!=null) {
			if(shows.size()>0) {
				for(Show show : shows) {
					show.setShowStatus(ShowStatus.ON_GOING);
					showDao.updateShow(show);
				}
			}
		}
	}
	
	@Scheduled(cron = "0 * * * * *")
	public void setShowToClosed() {
		List<Show> shows = showDao.getClosedShows(LocalDateTime.now(), ShowStatus.ON_GOING);
		if(shows!=null) {
			if(shows.size()>0) {
				System.err.println("hii");
				for(Show show : shows) {
					Screen screen = screenDao.getScreenById(show.getScreenId());
					screen.setScreenAvailability(ScreenAvailability.NOT_ALLOTTED);
					screen.setScreenstatus(Screenstatus.AVAILABLE);
					screenDao.saveScreen(screen);
					
					List<Ticket> tickets = ticketDao.getTicketsByShow(show);
					for(Ticket ticket : tickets) {
						ticket.setTicketStatus(TicketStatus.EXPIRED);
						List<Booking> bookings = ticket.getBookings();
						for(Booking booking : bookings) {
							if(booking.getBookingStatus().equals(BookingStatus.ACTIVE)) {
								booking.setBookingStatus(BookingStatus.EXPIRED);
								bookingDao.saveBooking(booking);
							}
						}
							ticket.setTicketStatus(TicketStatus.EXPIRED);
						ticketDao.bookTicket(ticket);
					}
					show.setShowStatus(ShowStatus.CLOSED);
					showDao.updateShow(show);
				}
			}
		}
	}
	
	

	
}
