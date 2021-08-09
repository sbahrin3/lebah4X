package lebah.db.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="log")
public class Log {
	
	@Id @Column(name="id", length=200)
	private String id;
	@Column(length=255)
	private String message;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;
	
	public Log() {
		setId(lebah.util.UIDGenerator.getUUID());
		setDateTime(new Date());
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public Date getDateTime() {
		return dateTime;
	}


	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	
}
