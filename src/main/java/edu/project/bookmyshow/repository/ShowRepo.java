package edu.project.bookmyshow.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.project.bookmyshow.entity.Show;
import edu.project.bookmyshow.enums.ShowStatus;

public interface ShowRepo extends JpaRepository<Show, Long> {
	
	
	@Query(value = "select s from Show s where s.showStartTime between ?1 and ?2")
	public Optional<List<Show>> getShowsIfPresentBetween(LocalDateTime startTime, LocalDateTime endTime);
	
	
	
	@Query(value = "select s from Show s where s.showLocation=?1 and s.showStatus=?2")
	public Optional<List<Show>> getShowsByCity(String city, ShowStatus showStatus);
	

	
	@Query(value = "select s from Show s where s.movieId=?1 and s.showStatus=?2")
	public Optional<List<Show>> getShowsByMovieId(long movieId, ShowStatus showStatus);
	
	
	
	@Query(value = "select s from Show s where s.showStartTime<=?1 and s.showStatus=?2")
	public Optional<List<Show>> getShowsByTime(LocalDateTime dateTime, ShowStatus active);
	
	

	@Query(value = "select s from Show s where s.showEndTime<=?1 and s.showStatus=?2")
	public Optional<List<Show>> getClosedShows(LocalDateTime dateTime, ShowStatus showStatus);
}
