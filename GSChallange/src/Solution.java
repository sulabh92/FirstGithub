import java.io.*;
import java.math.*;
import java.text.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.*;

public class Solution {
	static TreeMap<Date,Long> orderBookwithTime=new TreeMap<>();
	static TreeMap<Long, Order> orderBook=new TreeMap<>();
	static Date recent=null; 
	static TreeMap<String, Symbol> symbolMap;
	/*
	 * Complete the function below.
	 */
	static String[] processQueries(String[] queries) {

		// Write your code here.
		ArrayList<String> result=new ArrayList<>();
		for(String s:queries) {
			String[] orderDetails=s.split(",");
			if("N".equals(orderDetails[0])) {
				result.add(insert(orderDetails[1],orderDetails[2],orderDetails[3],orderDetails[4],orderDetails[5],orderDetails[6],orderDetails[7]));
			getSymbolMap();
			}
			if("A".equals(orderDetails[0])) {
				result.add(amend(orderDetails[1],orderDetails[2],orderDetails[3],orderDetails[4],orderDetails[5],orderDetails[6],orderDetails[7]));
			getSymbolMap();
			}
			if("X".equals(orderDetails[0])) {
				result.add(cancel(orderDetails[1],orderDetails[2]));
				getSymbolMap();
			}
			if("M".equals(orderDetails[0])) {
				getSymbolMap();
				if(orderDetails.length==2) { 
					String[] arr=(match(orderDetails[1]));
					for(String out:arr) {
						result.add(out);
					}
				}else {
					String[] arr=(match(orderDetails[1],orderDetails[2]));
					for(String out:arr) {
						result.add(out);
					}
				}
				cancelIocOrder();
			}
			if("Q".equals(orderDetails[0])) {
				getSymbolMap();
				if(orderDetails.length==1) { 
					String[] arr=(query());
					for(String out:arr) {
						
						result.add(out);
					}
				}
				else if(orderDetails.length==2) {
					try {
						Long timestamp=Long.parseLong(orderDetails[1]);
						String[] arr=(query(timestamp));
						for(String out:arr) {
							
							result.add(out);
						}
					}
					catch(Exception e){
						String[] arr=(query(orderDetails[1]));
						for(String out:arr) {
							
							result.add(out);
						}
					}
				}
				else if(orderDetails[0].length()==4) {
					try {
						Long timestamp=Long.parseLong(orderDetails[1]);
						String symbol=orderDetails[2];
						String[] arr=(query(timestamp,symbol));
						for(String out:arr) {
							
							result.add(out);
						}
					}
					catch(Exception e) {
						Long timestamp=Long.parseLong(orderDetails[2]);
						String symbol=orderDetails[1];
						String[] arr=(query(timestamp,symbol));
						for(String out:arr) {
							
							result.add(out);
						}
					}
				}
			}
		}
		String[] output=new String[result.size()];
		for(int i=0;i<result.size();i++) {
			
			output[i]=result.get(i);
		}
		return output;
	}



	private static String[] query(Long timestamp, String string) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		ArrayList<String> result=new ArrayList<>();
		Symbol symbol=symbolMap.get(string);
		if(symbol==null) {
			String[] s= {};
			return s;
		}
		for (Map.Entry<Double,TreeMap<Date, Order>> buylist : symbol.getBuyList().entrySet()) {
			Double buyPrice=buylist.getKey();
			TreeMap<Date, Order> timeArr=buylist.getValue();
			for(Map.Entry<Date, Order> t:timeArr.entrySet()) {
				if(orderBook.get(t.getValue().getOrderID()).isCancelled()||orderBook.get(t.getValue().getOrderID()).isMatched()) {
					
				}else {
					String s=null;
					if(new java.util.Date(t.getValue().getTimestamp()).compareTo(new Date(timestamp))<0){
				 s=matchBook(buyPrice,orderBook.get(t.getValue().getOrderID()),string,"Query");
					}if (s==null) {
					/*Order order=orderBook.get(t.getValue().getOrderID());
					result.add(symbol.getKey()+"|"+order.getOrderID()+","+order.getOrderType()+","
							+order.getQuantity()+","+buyPrice+"|");*/
					break;
				}else {
					result.add(s);
				}}
			}
			
		
		}
		getSymbolMap();
		
		for (Map.Entry<Double,TreeMap<Date, Order>> buylist : symbolMap.get(string).getBuyList().entrySet()) {
			for(Map.Entry<Date, Order> t:buylist.getValue().entrySet()) {
				Order order=t.getValue();
				if(!order.isCancelled()&&!order.isMatched()&&!order.isQuried()) {

if(new java.util.Date(order.getTimestamp()).compareTo(new Date(timestamp))<0){
					result.add(string+"|"+order.getOrderID()+","+order.getOrderType()+","
							+order.getQuantity()+","+order.getPrice()+"|");}
				}
			}
		}
		for (Map.Entry<Double,TreeMap<Date, Order>> selllist : symbolMap.get(string).getSellList().entrySet()) {
			for(Map.Entry<Date, Order> t:selllist.getValue().entrySet()) {
				Order order=t.getValue();
				if(!order.isCancelled()&&!order.isMatched()&&!order.isQuried()) {

if(new java.util.Date(order.getTimestamp()).compareTo(new Date(timestamp))<0){
					result.add(string+"|"+"|"+order.getPrice()+","+order.getQuantity()+","
							+order.getOrderType()+","+order.getOrderID());}
				}
			}
		}
		
		
		
		
		String[] output=new String[result.size()];
		for(int i=0;i<result.size();i++) {
			output[i]=result.get(i);
			if(i==4) {
				break;
			}
		}
		return output;
	
	
	
	}



	private static String[] query(String string) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		ArrayList<String> result=new ArrayList<>();
		Symbol symbol=symbolMap.get(string);
		if(symbol==null) {
			String[] s= {};
			return s;
		}
		for (Map.Entry<Double,TreeMap<Date, Order>> buylist : symbol.getBuyList().entrySet()) {
			Double buyPrice=buylist.getKey();
			TreeMap<Date, Order> timeArr=buylist.getValue();
			for(Map.Entry<Date, Order> t:timeArr.entrySet()) {
				if(orderBook.get(t.getValue().getOrderID()).isCancelled()||orderBook.get(t.getValue().getOrderID()).isMatched()) {
					
				}else {
					
				String s=matchBook(buyPrice,orderBook.get(t.getValue().getOrderID()),string,"Query");
				if (s==null) {
					/*Order order=orderBook.get(t.getValue().getOrderID());
					result.add(symbol.getKey()+"|"+order.getOrderID()+","+order.getOrderType()+","
							+order.getQuantity()+","+buyPrice+"|");*/
					break;
				}else {
					result.add(s);
				}}
			}
			
		
		}
		getSymbolMap();
		
		for (Map.Entry<Double,TreeMap<Date, Order>> buylist : symbolMap.get(string).getBuyList().entrySet()) {
			for(Map.Entry<Date, Order> t:buylist.getValue().entrySet()) {
				Order order=t.getValue();
				if(!order.isCancelled()&&!order.isMatched()&&!order.isQuried()) {
					result.add(string+"|"+order.getOrderID()+","+order.getOrderType()+","
							+order.getQuantity()+","+order.getPrice()+"|");
				}
			}
		}
		for (Map.Entry<Double,TreeMap<Date, Order>> selllist : symbolMap.get(string).getSellList().entrySet()) {
			for(Map.Entry<Date, Order> t:selllist.getValue().entrySet()) {
				Order order=t.getValue();
				if(!order.isCancelled()&&!order.isMatched()&&!order.isQuried()) {
					result.add(string+"|"+"|"+order.getPrice()+","+order.getQuantity()+","
							+order.getOrderType()+","+order.getOrderID());
				}
			}
		}
		
		
		
		
		String[] output=new String[result.size()];
		for(int i=0;i<result.size();i++) {
			output[i]=result.get(i);
			if(i==4) {
				break;
			}
		}
		return output;
	
	
	}



	private static String[] query(Long timestamp) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		ArrayList<String> result=new ArrayList<>();
		for (Entry<String, Symbol> symbol : symbolMap.entrySet()) {
		for (Map.Entry<Double,TreeMap<Date, Order>> buylist : symbol.getValue().getBuyList().entrySet()) {
			Double buyPrice=buylist.getKey();
			TreeMap<Date, Order> timeArr=buylist.getValue();
			for(Map.Entry<Date, Order> t:timeArr.entrySet()) {
				if(orderBook.get(t.getValue().getOrderID()).isCancelled()||orderBook.get(t.getValue().getOrderID()).isMatched()) {
					
				}else {String s=null;
					if(new java.util.Date(t.getValue().getTimestamp()).compareTo(new Date(timestamp))<0){
				 s=matchBook(buyPrice,orderBook.get(t.getValue().getOrderID()),symbol.getKey(),"Query");
					}if (s==null) {
					/*Order order=orderBook.get(t.getValue().getOrderID());
					result.add(symbol.getKey()+"|"+order.getOrderID()+","+order.getOrderType()+","
							+order.getQuantity()+","+buyPrice+"|");*/
					break;
				}else {
					result.add(s);
				}}
			}
			
		
		}
		getSymbolMap();
		
		for (Map.Entry<Double,TreeMap<Date, Order>> buylist : symbolMap.get(symbol.getKey()).getBuyList().entrySet()) {
			for(Map.Entry<Date, Order> t:buylist.getValue().entrySet()) {
				Order order=t.getValue();
				if(!order.isCancelled()&&!order.isMatched()&&!order.isQuried()) {
					if(new java.util.Date(order.getTimestamp()).compareTo(new Date(timestamp))<0){
					result.add(symbol.getKey()+"|"+order.getOrderID()+","+order.getOrderType()+","
							+order.getQuantity()+","+order.getPrice()+"|");}
				}
			}
		}
		for (Map.Entry<Double,TreeMap<Date, Order>> selllist : symbolMap.get(symbol.getKey()).getSellList().entrySet()) {
			for(Map.Entry<Date, Order> t:selllist.getValue().entrySet()) {
				Order order=t.getValue();
				if(!order.isCancelled()&&!order.isMatched()&&!order.isQuried()) {
					if(new java.util.Date(order.getTimestamp()).compareTo(new Date(timestamp))<0){
					result.add(symbol.getKey()+"|"+"|"+order.getPrice()+","+order.getQuantity()+","
							+order.getOrderType()+","+order.getOrderID());
				}}
			}
		}
		}
		
		
		
		String[] output=new String[result.size()];
		for(int i=0;i<result.size();i++) {
			output[i]=result.get(i);
			
		}
		return output;
	
	
	}



	private static void cancelIocOrder() {
		// TODO Auto-generated method stub
		for(Entry<Long,Order> entry:orderBook.entrySet()) {
			if(entry.getValue().getOrderType()=='I') {
				Order order=entry.getValue();
				order.setCancelled(true);
				orderBook.put(entry.getKey(), order);
			}
		}
	}



	private static String[] query() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		ArrayList<String> result=new ArrayList<>();
		for (Entry<String, Symbol> symbol : symbolMap.entrySet()) {
		for (Map.Entry<Double,TreeMap<Date, Order>> buylist : symbol.getValue().getBuyList().entrySet()) {
			Double buyPrice=buylist.getKey();
			TreeMap<Date, Order> timeArr=buylist.getValue();
			for(Map.Entry<Date, Order> t:timeArr.entrySet()) {
				if(orderBook.get(t.getValue().getOrderID()).isCancelled()||orderBook.get(t.getValue().getOrderID()).isMatched()) {
					
				}else {
					
				String s=matchBook(buyPrice,orderBook.get(t.getValue().getOrderID()),symbol.getKey(),"Query");
				if (s==null) {
					/*Order order=orderBook.get(t.getValue().getOrderID());
					result.add(symbol.getKey()+"|"+order.getOrderID()+","+order.getOrderType()+","
							+order.getQuantity()+","+buyPrice+"|");*/
					break;
				}else {
					result.add(s);
				}}
			}
			
		
		}
		getSymbolMap();
		
		for (Map.Entry<Double,TreeMap<Date, Order>> buylist : symbolMap.get(symbol.getKey()).getBuyList().entrySet()) {
			for(Map.Entry<Date, Order> t:buylist.getValue().entrySet()) {
				Order order=t.getValue();
				if(!order.isCancelled()&&!order.isMatched()&&!order.isQuried()) {
					result.add(symbol.getKey()+"|"+order.getOrderID()+","+order.getOrderType()+","
							+order.getQuantity()+","+order.getPrice()+"|");
				}
			}
		}
		for (Map.Entry<Double,TreeMap<Date, Order>> selllist : symbolMap.get(symbol.getKey()).getSellList().entrySet()) {
			for(Map.Entry<Date, Order> t:selllist.getValue().entrySet()) {
				Order order=t.getValue();
				if(!order.isCancelled()&&!order.isMatched()&&!order.isQuried()) {
					result.add(symbol.getKey()+"|"+"|"+order.getPrice()+","+order.getQuantity()+","
							+order.getOrderType()+","+order.getOrderID());
				}
			}
		}
		}
		
		
		
		String[] output=new String[result.size()];
		for(int i=0;i<result.size();i++) {
			output[i]=result.get(i);
			
		}
		return output;
	
	}



	private static String[] match(String timestamp,String symbol) {
		// TODO Auto-generated method stub
		ArrayList<String> result=new ArrayList<>();
		for (Map.Entry<Double,TreeMap<Date, Order>> buylist : symbolMap.get(symbol).getBuyList().entrySet()) {
			Double buyPrice=buylist.getKey();
			
			TreeMap<Date, Order> timeArr=buylist.getValue();
			for(Map.Entry<Date, Order> t:timeArr.entrySet()) {
				if(orderBook.get(t.getValue().getOrderID()).isCancelled()||orderBook.get(t.getValue().getOrderID()).isMatched()) {
					
				}else {
					
					
				String s=matchBook(buyPrice,orderBook.get(t.getValue().getOrderID()),symbol,"Match");
				if (s==null) {
					break;
				}else {
					result.add(s);
				}}
			}
			
		
		}
		String[] output=new String[result.size()];
		for(int i=0;i<result.size();i++) {
			output[i]=result.get(i);
		}
		return output;

	}


	private static String matchBook(Double buyPrice, Order value, String symbol, String requestType) {
		// TODO Auto-generated method stub
		String out=null;
Symbol symbolObj=symbolMap.get(symbol);
		if(value.getOrderType()=='L') {
			
			for(Map.Entry<Double, TreeMap<Date, Order>> entry:symbolMap.get(symbol).getSellList().entrySet()) {
				if(buyPrice>=entry.getKey()) {
					TreeMap<Date, Order> timearr=entry.getValue();
					for(Map.Entry<Date, Order> t:timearr.entrySet()) {
						if(requestType.equals("Match")) {
						if(orderBook.get(t.getValue().getOrderID()).isCancelled()||orderBook.get(t.getValue().getOrderID()).isMatched()) {
							continue;
						}}
						else{
							if(orderBook.get(t.getValue().getOrderID()).isQuried()) {
								continue;
							}
						}
						if(t.getValue().getQuantity()==value.getQuantity()) {
							Order sellOrder=orderBook.get(t.getValue().getOrderID());
							Order buyOrder=orderBook.get(value.getOrderID());
							
							
							if(requestType.equals("Match")) {
								sellOrder.setMatched(true);
								buyOrder.setMatched(true);
								sellOrder.setCurrentMatchedQuantity(sellOrder.getQuantity());
								buyOrder.setCurrentMatchedQuantity(buyOrder.getQuantity());
							orderBook.put(sellOrder.getOrderID(), sellOrder);
							orderBook.put(buyOrder.getOrderID(), buyOrder);
							symbolObj.setCurrentHighest(buyPrice);
							symbolObj.setCurrentLowest(sellOrder.getPrice());
							String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+sellOrder.getPrice();
							String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
							
							return symbol+"|"+matchedBuy+"|"+matchedSell;
							}
							
							else {
								sellOrder.setQuried(true);
								buyOrder.setQuried(true);
								
								String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+buyOrder.getPrice();
								String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
								
								return symbol+"|"+matchedBuy+"|"+matchedSell;
							}
						}
						//Buy Quantity is higer than sell Quantity
						else if(t.getValue().getQuantity()<value.getQuantity()) {
							Order sellOrder=orderBook.get(t.getValue().getOrderID());
							Order buyOrder=orderBook.get(value.getOrderID());
							
							if(requestType.equals("Match")) {
								buyOrder.setCurrentMatchedQuantity(sellOrder.getQuantity());
								buyOrder.setRemainingQuantity(buyOrder.getQuantity()-buyOrder.getCurrentMatchedQuantity());
								
								sellOrder.setMatched(true);
								buyOrder.setPartiallyexecuted(true);
								sellOrder.setCurrentMatchedQuantity(sellOrder.getQuantity());
							orderBook.put(sellOrder.getOrderID(), sellOrder);
							orderBook.put(buyOrder.getOrderID(), buyOrder);
							
							String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+sellOrder.getQuantity()+","+sellOrder.getPrice();
							String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
							new ArrayList<>();
							
							return symbol+"|"+matchedBuy+"|"+matchedSell;}else {
								sellOrder.setQuried(true);
								buyOrder.setQuried(true);
								String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+buyOrder.getPrice();
								String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
								
								return symbol+"|"+matchedBuy+"|"+matchedSell;
							}
						}
						else {
							Order sellOrder=orderBook.get(t.getValue().getOrderID());
							Order buyOrder=orderBook.get(value.getOrderID());
							
							
							if(requestType.equals("Match")) {
								sellOrder.setCurrentMatchedQuantity(buyOrder.getQuantity());
								sellOrder.setRemainingQuantity(sellOrder.getQuantity()-buyOrder.getQuantity());
								buyOrder.setCurrentMatchedQuantity(buyOrder.getQuantity());
								sellOrder.setPartiallyexecuted(true);
								buyOrder.setMatched(true);
							orderBook.put(sellOrder.getOrderID(), sellOrder);
							orderBook.put(buyOrder.getOrderID(), buyOrder);
							String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+sellOrder.getPrice();
							String matchedSell=buyOrder.getPrice()+","+sellOrder.getQuantity()+","+buyOrder.getOrderType()+","+sellOrder.getOrderID();
							new ArrayList<>();
							
							return symbol+"|"+matchedBuy+"|"+matchedSell;}
							else {
								sellOrder.setQuried(true);
								buyOrder.setQuried(true);
								String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+buyOrder.getPrice();
								String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
								
								return symbol+"|"+matchedBuy+"|"+matchedSell;
							}
						}
					}
					
				}
				else {
					break;
				}
			}
			
		}
		else if(value.getOrderType()=='M') {
			
			for(Map.Entry<Double, TreeMap<Date, Order>> entry:symbolMap.get(symbol).getSellList().entrySet()) {
				if(buyPrice==entry.getKey()) {
					
					TreeMap<Date, Order> timearr=entry.getValue();
					for(Map.Entry<Date, Order> t:timearr.entrySet()) {
						if(requestType.equals("Match")) {
							if(orderBook.get(t.getValue().getOrderID()).isCancelled()||orderBook.get(t.getValue().getOrderID()).isMatched()) {
								continue;
							}}
							else{
								if(orderBook.get(t.getValue().getOrderID()).isQuried()) {
									continue;
								}
							}
						if(t.getValue().getQuantity()==value.getQuantity()) {
							Order sellOrder=orderBook.get(t.getValue().getOrderID());
							Order buyOrder=orderBook.get(value.getOrderID());
							sellOrder.setMatched(true);
							buyOrder.setMatched(true);
							sellOrder.setCurrentMatchedQuantity(sellOrder.getQuantity());
							buyOrder.setCurrentMatchedQuantity(buyOrder.getQuantity());
							if(requestType.equals("Match")) {
							orderBook.put(sellOrder.getOrderID(), sellOrder);
							orderBook.put(buyOrder.getOrderID(), buyOrder);
							String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+buyOrder.getPrice();
							String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
							symbolObj.setCurrentHighest(buyPrice);
							symbolObj.setCurrentLowest(sellOrder.getPrice());
							
							return symbol+"|"+matchedBuy+"|"+matchedSell;}else {
								sellOrder.setQuried(true);
								buyOrder.setQuried(true);
								String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+buyOrder.getPrice();
								String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
								
								return symbol+"|"+matchedBuy+"|"+matchedSell;
							}
						}
						//Buy Quantity is higer than sell Quantity
						else if(t.getValue().getQuantity()<value.getQuantity()) {
							Order sellOrder=orderBook.get(t.getValue().getOrderID());
							Order buyOrder=orderBook.get(value.getOrderID());
							sellOrder.setMatched(true);
							buyOrder.setPartiallyexecuted(true);
							buyOrder.setCurrentMatchedQuantity(sellOrder.getQuantity());
							buyOrder.setRemainingQuantity(buyOrder.getQuantity()-buyOrder.getCurrentMatchedQuantity());
							sellOrder.setCurrentMatchedQuantity(sellOrder.getQuantity());
							if(requestType.equals("Match")) {
							orderBook.put(sellOrder.getOrderID(), sellOrder);
							orderBook.put(buyOrder.getOrderID(), buyOrder);
							String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+sellOrder.getQuantity()+","+sellOrder.getPrice();
							String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
						
							return symbol+"|"+matchedBuy+"|"+matchedSell;}else {
								sellOrder.setQuried(true);
								buyOrder.setQuried(true);
								String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+buyOrder.getPrice();
								String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
								
								return symbol+"|"+matchedBuy+"|"+matchedSell;
							}
						}
						else {
							Order sellOrder=orderBook.get(t.getValue().getOrderID());
							Order buyOrder=orderBook.get(value.getOrderID());
							sellOrder.setPartiallyexecuted(true);
							buyOrder.setMatched(true);
							sellOrder.setCurrentMatchedQuantity(buyOrder.getQuantity());
							sellOrder.setRemainingQuantity(sellOrder.getQuantity()-buyOrder.getQuantity());
							buyOrder.setCurrentMatchedQuantity(buyOrder.getQuantity());
							if(requestType.equals("Match")) {
							orderBook.put(sellOrder.getOrderID(), sellOrder);
							orderBook.put(buyOrder.getOrderID(), buyOrder);
							String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+sellOrder.getPrice();
							String matchedSell=buyOrder.getPrice()+","+sellOrder.getQuantity()+","+buyOrder.getOrderType()+","+sellOrder.getOrderID();
						
							return symbol+"|"+matchedBuy+"|"+matchedSell;}
							else {
								sellOrder.setQuried(true);
								buyOrder.setQuried(true);
								String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+buyOrder.getPrice();
								String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
								
								return symbol+"|"+matchedBuy+"|"+matchedSell;
							}
						}
					}
					
				}
				else {
					break;
				}
			}
		}
		else if(value.getOrderType()=='I') {
			for(Map.Entry<Double, TreeMap<Date, Order>> entry:symbolMap.get(symbol).getSellList().entrySet()) {
				if(buyPrice>=entry.getKey()) {
					TreeMap<Date, Order> timearr=entry.getValue();
					for(Map.Entry<Date, Order> t:timearr.entrySet()) {
						if(requestType.equals("Match")) {
							if(orderBook.get(t.getValue().getOrderID()).isCancelled()||orderBook.get(t.getValue().getOrderID()).isMatched()) {
								continue;
							}}
							else{
								if(orderBook.get(t.getValue().getOrderID()).isQuried()) {
									continue;
								}
							}
						if(t.getValue().getQuantity()==value.getQuantity()) {
							Order sellOrder=orderBook.get(t.getValue().getOrderID());
							Order buyOrder=orderBook.get(value.getOrderID());
							sellOrder.setMatched(true);
							buyOrder.setMatched(true);
							sellOrder.setCurrentMatchedQuantity(sellOrder.getQuantity());
							buyOrder.setCurrentMatchedQuantity(buyOrder.getQuantity());
							if(requestType.equals("Match")) {
							orderBook.put(sellOrder.getOrderID(), sellOrder);
							orderBook.put(buyOrder.getOrderID(), buyOrder);
							String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+buyOrder.getPrice();
							String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
							symbolObj.setCurrentHighest(buyPrice);
							symbolObj.setCurrentLowest(sellOrder.getPrice());
						
							return symbol+"|"+matchedBuy+"|"+matchedSell;}else {
								sellOrder.setQuried(true);
								buyOrder.setQuried(true);
								String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+buyOrder.getPrice();
								String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
								
								return symbol+"|"+matchedBuy+"|"+matchedSell;
							}
						}
						//Buy Quantity is higer than sell Quantity
						else if(t.getValue().getQuantity()<value.getQuantity()) {
							Order sellOrder=orderBook.get(t.getValue().getOrderID());
							Order buyOrder=orderBook.get(value.getOrderID());
							sellOrder.setMatched(true);
							buyOrder.setPartiallyexecuted(true);
							buyOrder.setCurrentMatchedQuantity(sellOrder.getQuantity());
							buyOrder.setRemainingQuantity(buyOrder.getQuantity()-buyOrder.getCurrentMatchedQuantity());
							sellOrder.setCurrentMatchedQuantity(sellOrder.getQuantity());
							if(requestType.equals("Match")) {
							orderBook.put(sellOrder.getOrderID(), sellOrder);
							orderBook.put(buyOrder.getOrderID(), buyOrder);
							String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+sellOrder.getQuantity()+","+sellOrder.getPrice();
							String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
							
							return symbol+"|"+matchedBuy+"|"+matchedSell;}else {
								sellOrder.setQuried(true);
								buyOrder.setQuried(true);
								String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+buyOrder.getPrice();
								String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
								
								return symbol+"|"+matchedBuy+"|"+matchedSell;
							}
						}
						else {
							Order sellOrder=orderBook.get(t.getValue().getOrderID());
							Order buyOrder=orderBook.get(value.getOrderID());
							sellOrder.setPartiallyexecuted(true);
							buyOrder.setMatched(true);
							sellOrder.setCurrentMatchedQuantity(buyOrder.getQuantity());
							sellOrder.setRemainingQuantity(sellOrder.getQuantity()-buyOrder.getQuantity());
							buyOrder.setCurrentMatchedQuantity(buyOrder.getQuantity());
							if(requestType.equals("Match")) {
							orderBook.put(sellOrder.getOrderID(), sellOrder);
							orderBook.put(buyOrder.getOrderID(), buyOrder);
							String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+sellOrder.getPrice();
							String matchedSell=buyOrder.getPrice()+","+sellOrder.getQuantity()+","+buyOrder.getOrderType()+","+sellOrder.getOrderID();
														
							return symbol+"|"+matchedBuy+"|"+matchedSell;}
							else {
								sellOrder.setQuried(true);
								buyOrder.setQuried(true);
								String matchedBuy=buyOrder.getOrderID()+","+buyOrder.getOrderType()+","+buyOrder.getQuantity()+","+buyOrder.getPrice();
								String matchedSell=sellOrder.getPrice()+","+sellOrder.getQuantity()+","+sellOrder.getOrderType()+","+sellOrder.getOrderID();
								
								return symbol+"|"+matchedBuy+"|"+matchedSell;
							}
						}
					}
					
				}
				else {
					break;
				}
			}
		}
		
		return out;
	}


	private static String[] match(String timestamp) {
		// TODO Auto-generated method stub
		ArrayList<String> result=new ArrayList<>();
		for (Entry<String, Symbol> symbol : symbolMap.entrySet()) {
		for (Map.Entry<Double,TreeMap<Date, Order>> buylist : symbol.getValue().getBuyList().entrySet()) {
			Double buyPrice=buylist.getKey();
			TreeMap<Date, Order> timeArr=buylist.getValue();
			for(Map.Entry<Date, Order> t:timeArr.entrySet()) {
				if(orderBook.get(t.getValue().getOrderID()).isCancelled()||orderBook.get(t.getValue().getOrderID()).isMatched()) {
					
				}else {
				String s=matchBook(buyPrice,orderBook.get(t.getValue().getOrderID()),symbol.getKey(),"Match");
				if (s==null) {
					break;
				}else {
					result.add(s);
				}}
			}
			
		
		}
		}
		String[] output=new String[result.size()];
		for(int i=0;i<result.size();i++) {
			output[i]=result.get(i);
		}
		return output;
	}


	private static String cancel(String orderID, String timestamp) {
		// TODO Auto-generated method stub
		if(recent==null) {
			recent =new Date(Long.parseLong( timestamp));
		}
		if(recent.compareTo(new Date(Long.parseLong( timestamp)))>0) {
			//System.out.println("inside date");
			recent=new Date(Long.parseLong( timestamp));
			return orderID+" – CancelReject - 404 - Order does not exist";
		}else {
			recent=new Date(Long.parseLong( timestamp));
			if(orderBook.containsKey(Long.parseLong(orderID))){
				Order order=orderBook.get(Long.parseLong(orderID));
				if(order.isCancelled()||order.isMatched()) {
					return orderID+" – CancelReject - 404 - Order does not exist";
				}
				else {
					order.setCancelled(true);
					orderBook.put(Long.parseLong(orderID), order);
					return orderID+" - CancelAccept";
				}
			}
			else {
				return orderID+" – CancelReject - 404 - Order does not exist";
			}
		}
	}


	private static String amend(String orderID, String timestamp, String symbol, String orderType, String side,
			String price, String quantity) {
		// TODO Auto-generated method stub
		if(orderType.charAt(0)!='M'&&orderType.charAt(0)!='L'&&orderType.charAt(0)!='I') {
			return orderID+" - AmendReject - 101 - Invalid amendment details";
		}
		if(side.charAt(0)!='B'&&side.charAt(0)!='S') {
			return orderID+" - AmendReject - 101 - Invalid amendment details";
		}
		if(recent==null) {
			recent =new Date(Long.parseLong( timestamp));
		}
		if(recent.compareTo(new Date(Long.parseLong( timestamp)))>0) {
			//System.out.println("inside date");
			recent=new Date(Long.parseLong( timestamp));
			return orderID+" - AmendReject - 101 - Invalid amendment details";
		}
		recent=new Date(Long.parseLong( timestamp));
		if(!orderBook.containsKey(Long.parseLong(  orderID))) {
			return orderID+" - AmendReject - 404 - Order does not exist";
		}
		else {
			Order order=orderBook.get(Long.parseLong(  orderID));
			if(order.isCancelled()||order.isMatched()) {
				return orderID+" - AmendReject - 101 - Invalid amendment details";
			}
			else {try {
				if(order.getSymbol().equals(symbol)&&
						order.getOrderType()==orderType.charAt(0)&&order.getSide()==side.charAt(0)) {
					if(order.getQuantity()!=Long.parseLong(quantity)) {
						if(order.isPartiallyexecuted()) {
							if(Long.parseLong(quantity)<=order.getCurrentMatchedQuantity()) {
								order.setMatched(true);
								order.setQuantity(Long.parseLong(quantity));
								order.setRemainingQuantity(0);
								order.setTimestamp(Long.parseLong(timestamp));
								orderBook.put(Long.parseLong(orderID), order);
								getSymbolMap();
								return orderID+" - AmendAccept";
							}
							else {
								order.setRemainingQuantity(Long.parseLong(quantity)-order.getCurrentMatchedQuantity());
								order.setQuantity(Long.parseLong(quantity));
								order.setTimestamp(Long.parseLong(timestamp));
								orderBook.put(Long.parseLong(orderID), order);
								getSymbolMap();
								return orderID+" - AmendAccept";
							}
						}else {
							order.setQuantity(Long.parseLong(quantity));
							order.setPrice(Double.parseDouble(price));
							order.setTimestamp(Long.parseLong(timestamp));
							orderBook.put(Long.parseLong(orderID), order);
							getSymbolMap();
							return orderID+" - AmendAccept";

						}}else {
							order.setPrice(Double.parseDouble(price));
							order.setTimestamp(Long.parseLong(timestamp));
							orderBook.put(Long.parseLong(orderID), order);
							getSymbolMap();
							return orderID+" - AmendAccept";
						}
				}else {
					return orderID+" - AmendReject - 101 - Invalid amendment details";
				}}
			catch(Exception e) {
				return orderID+" - AmendReject - 101 - Invalid amendment details";
			}
			}
		}
	}


	private static  String insert(String orderID, String timestamp, String symbol, String orderType, String side,
			String price, String quantity) {
		// TODO Auto-generated method stub
		if(orderType.charAt(0)!='M'&&orderType.charAt(0)!='L'&&orderType.charAt(0)!='I') {
			return orderID+" - Reject - 303 - Invalid order details";
		}
		if(side.charAt(0)!='B'&&side.charAt(0)!='S') {
			return orderID+" - Reject - 303 - Invalid order details";
		}
		if(recent==null) {
			recent =new Date(Long.parseLong( timestamp));
		}
		if(orderBook.containsKey(Long.parseLong(  orderID))||recent.compareTo(new Date(Long.parseLong( timestamp)))>0) {
			recent=new Date(Long.parseLong( timestamp));
			return orderID+" - Reject - 303 - Invalid order details";
		}
		else {recent=new Date(Long.parseLong( timestamp));
		try {
			Order newOrder=new Order(Long.parseLong(  orderID), Long.parseLong( timestamp), symbol, orderType.toCharArray()[0], side.toCharArray()[0],Double.parseDouble(price), Long.parseLong( quantity));
			if(Double.parseDouble(price)<0 || Long.parseLong( quantity)<0) {
				return orderID+" - Reject - 303 - Invalid order details";
			}
			orderBook.put(Long.parseLong(  orderID), newOrder);
			
			recent =new Date(Long.parseLong( timestamp));
			return orderID+" - Accept";

		}
		catch (Exception e) {
			return orderID+" - Reject - 303 - Invalid order details";
		}
		}

	}

	private static void getSymbolMap() {
		// TODO Auto-generated method stub
		symbolMap=new TreeMap<>();
		
		for (Map.Entry<Long, Order> entry : orderBook.entrySet()) {
			if(!entry.getValue().isCancelled()||!entry.getValue().isMatched()) {
				if(symbolMap.containsKey(entry.getValue().getSymbol())) {


					Symbol symbol=symbolMap.get(entry.getValue().getSymbol());
					if('B'==entry.getValue().getSide()) {
						
						TreeMap<Double,TreeMap<Date,Order>> buyList=symbol.getBuyList();
						if(buyList.containsKey(entry.getValue().getPrice())) {
							TreeMap<Date,Order> timeArr=buyList.get(entry.getValue().getPrice());
							timeArr.put(new java.util.Date(entry.getValue().getTimestamp()), entry.getValue());
							buyList.put(entry.getValue().getPrice(), timeArr);
							symbol.setBuyList(buyList);
						}
						else {
							TreeMap<Date,Order> timeArr=new TreeMap<>();
							timeArr.put(new java.util.Date(entry.getValue().getTimestamp()), entry.getValue());
							buyList.put(entry.getValue().getPrice(), timeArr);
							symbol.setBuyList(buyList);
						}
					}
					else {
						TreeMap<Double,TreeMap<Date,Order>> sellList=symbol.getSellList();
						if(sellList.containsKey(entry.getValue().getPrice())) {
							TreeMap<Date,Order> timeArr=sellList.get(entry.getValue().getPrice());
							timeArr.put(new java.util.Date(entry.getValue().getTimestamp()), entry.getValue());
							sellList.put(entry.getValue().getPrice(), timeArr);
							symbol.setSellList(sellList);
						}
						else {
							
							TreeMap<Date,Order> timeArr=new TreeMap<>();
							timeArr.put(new java.util.Date(entry.getValue().getTimestamp()), entry.getValue());
							sellList.put(entry.getValue().getPrice(), timeArr);
							symbol.setSellList(sellList);
						}
					}

					symbolMap.put(entry.getValue().getSymbol(), symbol);
				}
				else {
					Symbol symbol=new Symbol();
					symbol.setName(entry.getValue().getSymbol());
					if('B'==entry.getValue().getSide()) {
						
						TreeMap<Double,TreeMap<Date,Order>> buyList=new TreeMap<>();
						TreeMap<Date,Order> timeArr=new TreeMap<>();
						timeArr.put(new java.util.Date(entry.getValue().getTimestamp()), entry.getValue());
						buyList.put(entry.getValue().getPrice(), timeArr);
						symbol.setBuyList(buyList);
					}else {
					
						TreeMap<Double,TreeMap<Date,Order>> sellList=new TreeMap<>(Collections.reverseOrder());
						TreeMap<Date,Order> timeArr=new TreeMap<>();
						timeArr.put(new java.util.Date(entry.getValue().getTimestamp()), entry.getValue());
						sellList.put(entry.getValue().getPrice(), timeArr);
						symbol.setSellList(sellList);
					}
					symbolMap.put(entry.getValue().getSymbol(), symbol);
				}
			}
		}


	}

	private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        if (bw == null) {
            bw = new BufferedWriter(new OutputStreamWriter(System.out));
        }

        int queriesSize = Integer.parseInt(scan.nextLine().trim());

        String[] queries = new String[queriesSize];

        for (int queriesItr = 0; queriesItr < queriesSize; queriesItr++) {
            String queriesItem = scan.nextLine();
            queries[queriesItr] = queriesItem;

        }

        String[] response = processQueries(queries);

        for (int responseItr = 0; responseItr < response.length; responseItr++) {
            bw.write(response[responseItr]);

            if (responseItr != response.length - 1) {
                bw.write("\n");
            }
        }

        bw.newLine();

        bw.close();
    }
}
/*	private static final Scanner scan = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		    BufferedWriter bw = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        if (bw == null) {
            bw = new BufferedWriter(new OutputStreamWriter(System.out));
        }

		//  int queriesSize = Integer.parseInt(scan.nextLine().trim());

		// String[] queries = new String[queriesSize];
		String[] queries= {"N,1,0000001,AB,L,B,104.53,100","N,2,0000002,AB,L,S,105.53,100","N,3,0000003,AB,L,B,104.53,90","M,0000004","N,4,0000005,AB,L,S,104.43,80","A,2,0000006,AB,L,S,104.42,100","Q","M,0000008","N,5,0000009,AB,L,S,105.53,120","X,3,0000010","N,6,0000011,XYZ,L,B,1214.82,2568","Q"};
		  for (int queriesItr = 0; queriesItr < queriesSize; queriesItr++) {
            String queriesItem = scan.nextLine();
            queries[queriesItr] = queriesItem;

        }

		String[] response = processQueries(queries);

		for (int responseItr = 0; responseItr < response.length; responseItr++) {
			// bw.write(response[responseItr]);
			System.out.println(response[responseItr]);
			if (responseItr != response.length - 1) {
				//bw.write("\n");

			}
		}

		//bw.newLine();

		// bw.close();
	}
}*/
class Order{
	private long orderID;
	private long timestamp;
	private String symbol;
	private char orderType;
	private char side;
	private double price;
	private long quantity;
	private long remainingQuantity;
	private long currentMatchedQuantity;
	private boolean partiallyexecuted=false;
	private boolean quried=false;
	public boolean isQuried() {
		return quried;
	}
	public void setQuried(boolean quried) {
		this.quried = quried;
	}
	public long getCurrentMatchedQuantity() {
		return currentMatchedQuantity;
	}
	public void setCurrentMatchedQuantity(long currentMatchedQuantity) {
		this.currentMatchedQuantity = currentMatchedQuantity;
	}
	public boolean isPartiallyexecuted() {
		return partiallyexecuted;
	}
	public void setPartiallyexecuted(boolean partiallyexecuted) {
		this.partiallyexecuted = partiallyexecuted;
	}
	private boolean matched=false;
	private boolean cancelled=false;

	public long getRemainingQuantity() {
		return remainingQuantity;
	}
	public void setRemainingQuantity(long remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}
	public boolean isMatched() {
		return matched;
	}
	public void setMatched(boolean matched) {
		this.matched = matched;
	}
	public boolean isCancelled() {
		return cancelled;
	}
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	public Order(long orderID, long timestamp, String symbol, char orderrType, char side, double price, long quantity) {
		super();
		this.orderID = orderID;
		this.timestamp = timestamp;
		this.symbol = symbol;
		this.orderType = orderrType;
		this.side = side;
		this.price = price;
		this.quantity = quantity;
	}
	public long getOrderID() {
		return orderID;
	}
	public void setOrderID(long orderID) {
		this.orderID = orderID;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public char getOrderType() {
		return orderType;
	}
	public void setOrderType(char orderrType) {
		this.orderType = orderrType;
	}
	public char getSide() {
		return side;
	}
	public void setSide(char side) {
		this.side = side;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

}
class Symbol{
	private String name;
	private Double currentHighest;
	private Double currentLowest;
	private TreeMap<Double,TreeMap<Date,Order>> buyList=new TreeMap<>();
	private TreeMap<Double,TreeMap<Date,Order>> sellList=new TreeMap<>();

	public Double getCurrentHighest() {
		return currentHighest;
	}
	public void setCurrentHighest(Double currentHighest) {
		this.currentHighest = currentHighest;
	}
	public Double getCurrentLowest() {
		return currentLowest;
	}
	public void setCurrentLowest(Double currentLowest) {
		this.currentLowest = currentLowest;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TreeMap<Double, TreeMap<Date, Order>> getBuyList() {
		return buyList;
	}
	public void setBuyList(TreeMap<Double, TreeMap<Date, Order>> buyList) {
		this.buyList = buyList;
	}
	public TreeMap<Double, TreeMap<Date, Order>> getSellList() {
		return sellList;
	}
	public void setSellList(TreeMap<Double, TreeMap<Date, Order>> sellList) {
		this.sellList = sellList;
	}


}