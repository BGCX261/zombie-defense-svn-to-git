package com.tfu.pathing;

import java.util.ArrayList;

public class Path {
	@SuppressWarnings("rawtypes")
	private ArrayList steps = new ArrayList();
	
	public Path() {
		
	}
	
	public int getLength() {
		return steps.size();
	}
	
	public Step getStep(int index) {
		return (Step)steps.get(index);
	}
	
	public int getX(int index) {
		return getStep(index).x;
	}
	
	public int getY(int index) {
		return getStep(index).y;
	}
	
	@SuppressWarnings("unchecked")
	public void appendStep(int x, int y) {
		steps.add(new Step(x,y));
	}
	
	@SuppressWarnings("unchecked")
	public void prependStep(int x, int y) {
		steps.add(0, new Step(x, y));
	}
	
	public boolean contains(int x, int y) {
		return steps.contains(new Step(x,y));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		int len = steps.size();
		for (int i=0; i<len; i++) {
			Step step = (Step)steps.get(i);
			sb.append("[");
			sb.append(step.getX());
			sb.append(",");
			sb.append(step.getY());
			sb.append("]->");
		}
		
		return sb.toString();
	}

	
	public class Step {
		private int x;
		private int y;
		
		public Step(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public int hashCode() {
			return x*y;
		}
		
		public boolean equals(Object other) {
			if (other instanceof Step) {
				Step o = (Step) other;
				return (o.x == x) && (o.y == y);
			}
			return false;
		}
	}
}
