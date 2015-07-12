package com.bigmouth.app.bean;

public class Readings {
	private String title;
	private String id;
	private String text = "";
	private String img;
	private String date;
	private int RandomColor;
	private Boolean isShowReads = false;

	public int getRandomColor() {
		return RandomColor;
	}

	public void setRandomColor(int randomColor) {
		RandomColor = randomColor;
	}

	public Boolean getIsShowReads() {
		return isShowReads;
	}

	public void setIsShowReads(Boolean isShowReads) {
		this.isShowReads = isShowReads;
	}

	public Boolean getIsHaveReads() {
		return isHaveReads;
	}

	public void setIsHaveReads(Boolean isHaveReads) {
		this.isHaveReads = isHaveReads;
	}

	public Boolean getIsHaveColor() {
		return isHaveColor;
	}

	public void setIsHaveColor(Boolean isHaveColor) {
		this.isHaveColor = isHaveColor;
	}

	private String source;
	private Boolean isHaveReads = false;
	private Boolean isHaveColor = false;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
