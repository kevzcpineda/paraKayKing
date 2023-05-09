package quest.phil.diagnostic.information.system.ws.model.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;


public class DenominationReportsRequest implements Serializable  {


	private static final long serialVersionUID = 2874563622514351306L;
	private int thousands;
	private int fiveHundreds;
	private int twoHundreds;
	private int oneHundreds;
	private int fifties;
	private int twenties;
	private int tens;
	private int five;
	private int one;
	private int cents;
	
	public int getThousands() {
		return thousands;
	}
	public int getFiveHundreds() {
		return fiveHundreds;
	}
	public int getTwoHundreds() {
		return twoHundreds;
	}
	public int getOneHundreds() {
		return oneHundreds;
	}
	public int getFifties() {
		return fifties;
	}
	public int getTwenties() {
		return twenties;
	}
	public int getTens() {
		return tens;
	}
	public int getFive() {
		return five;
	}
	public int getOne() {
		return one;
	}
	public int getCents() {
		return cents;
	}
	public void setThousands(int thousands) {
		this.thousands = thousands;
	}
	public void setFiveHundreds(int fiveHundreds) {
		this.fiveHundreds = fiveHundreds;
	}
	public void setTwoHundreds(int twoHundreds) {
		this.twoHundreds = twoHundreds;
	}
	public void setOneHundreds(int oneHundreds) {
		this.oneHundreds = oneHundreds;
	}
	public void setFifties(int fifties) {
		this.fifties = fifties;
	}
	public void setTwenties(int twenties) {
		this.twenties = twenties;
	}
	public void setTens(int tens) {
		this.tens = tens;
	}
	public void setFive(int five) {
		this.five = five;
	}
	public void setOne(int one) {
		this.one = one;
	}
	public void setCents(int cents) {
		this.cents = cents;
	}
	
}
