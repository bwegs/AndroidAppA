package myApp.list;

public class AlertListItem {
	private String title;   // name of the alert
	private int icon;	    // id for email or text icon
	private String contact; // phone # or email
	private String when;    // "enter" or "exit"
	private String message; // the message being sent
	private String location; // still unsure
	
	public AlertListItem(){
		this.title = "Title (e.g. Leaving School)";
		this.contact = "Contact (e.g. Mom)";
		this.message = "Hey mom, I'm leaving school now. See you soon!";
	}

	public AlertListItem(String title, int icon, String contact, String message){
		this.title = title;
		this.icon = icon;
		this.contact = contact;
		this.message = message;
	}
	
	public AlertListItem(String title, int icon, String when, String contact, String message){
		this.title = title;
		this.icon = icon;
		this.when = when;
		this.contact = contact;
		this.message = message;
	}
	
	public AlertListItem(String title, String contact, String location, String message, String when, int icon){
		this.title = title;
		this.icon = icon;
		this.when = when;
		this.contact = contact;
		this.message = message;
		this.location = location;
	}

	public String getTitle(){
		return this.title;
	}
	
	public int getIcon(){
		return this.icon;
	}
	
	public String getContact(){
		return this.contact;
	}
	
	public String getWhen(){
		return this.when;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(int icon){
		this.icon = icon;
	}
	
	public void setContact(String contact){
		this.contact = contact;
	}
	
	public void setWhen(String when){
		this.when = when;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
