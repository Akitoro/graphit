package de.akitoro.graphit.core;

public class Vertex implements Comparable<Vertex>{
	
	public final String id;
	public Properties properties = new Properties();
	
	public Vertex(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id.toString();
	}

	@Override
	public int compareTo(Vertex o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		else if(obj instanceof Vertex) {
			Vertex other = (Vertex) obj;
			return this.id.equals(other.id);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return id.toString().hashCode();
	}
}
