package crawler;

import java.io.Serializable;

public class Ranking implements Comparable<Ranking>, Serializable {
	private static final long serialVersionUID = 1L;
	private String path;
	private float result;

	public Ranking(String path, float result) {
		this.path = path;
		this.result = result;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public float getResult() {
		return result;
	}

	public void setResult(float result) {
		this.result = result;
	}

	@Override
	public int compareTo(Ranking o) {

		if (this.result > o.getResult()) {
			return -1;
		} else if (this.result < o.getResult()) {
			return 1;
		} else {
			return 0;
		}
	}

}
