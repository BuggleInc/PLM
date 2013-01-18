package jlm.universe.bat;

public class BatTest {
	Object[] parameters;
	
	protected Object result;
	protected Object expected;
	
	private boolean visible;
	private boolean correct,answered;
	protected boolean objectiveTest=false;
	private String funName;
	
	public BatTest(String funName, boolean visible,Object parameters) {
		this.funName = funName;
		this.visible = visible;
		this.correct = false;
		this.answered = false;
		
		/* Cast parameters into an array on need */
		if (parameters.getClass().isArray()) {
			this.parameters = (Object[]) parameters;
		} else {
			this.parameters = new Object[] {parameters};
		}
	}
	public BatTest copy() {
		BatTest res = new BatTest(funName,visible,parameters.clone());
		res.expected = expected;
		return res;
	}
	
	public void setResult(Object r) {
		result = r;
		
		if (expected != null)
			correct = expected.equals(result);
		answered = true;
	}
	public boolean isVisible() {
		return visible;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BatTest)) 
			return false;
		BatTest other = (BatTest) o;
		if (other.parameters.length != parameters.length) {
			//System.out.println("While comparing a Bat test, the amount of parameters differs: "+parameters.length+" != "+other.parameters.length);
			return false;
		}
		for (int i=0;i<parameters.length;i++)
			if (!parameters[i].equals(other.parameters[i])) {
				//System.out.println("While comparing a Bat test, the parameter "+i+" differs: "+parameters[i]+" != "+other.parameters[i]);
				return false;
			}
		if (result == null && other.result != null) {
			//System.out.println("While comparing a Bat test, the result differs: null != "+other.result);
			return false;			
		}
		if (result !=null && !result.equals(other.result)) {
			//System.out.println("While comparing a Bat test, the result differs: "+result+" != "+other.result);
			return false;
		}
		if (!expected.equals(other.expected)) {
			//System.out.println("While comparing a Bat test, the expected value differs: "+expected+" != "+other.expected);
			return false;
		}
		return true;
	}

	public Object getParameter(int i) {
		return parameters[i];
	}

	public boolean isAnswered() {
		return answered;
	}
	public boolean isCorrect() {
		return correct;
	}
	
	private String name = null;

	
	public String getName() {
		if (name == null) {
			StringBuffer sb=new StringBuffer(funName+"(");
			
			for (Object o:parameters) {
				if (o instanceof String[]) {
					sb.append("{");
					String[]a = (String[]) o;
					for (String i:a) {
						sb.append(i+",");
					}
					sb.deleteCharAt(sb.length()-1);
					sb.append("},");					
				} else if (o.getClass().isArray()){
					sb.append("{");
					if (o.getClass().getComponentType().equals(Integer.TYPE)) {
						int[]a = (int[]) o;
						for (int i:a) {
							sb.append(i+",");
						}
					} else {
						throw new RuntimeException("Unhandled internal type (only integer arrays are handled so far)");
					}
					sb.deleteCharAt(sb.length()-1);
					sb.append("},");					
				} else {
					sb.append(o.toString()+",");
				}
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")");					
			name=sb.toString();

		}
		return name;
	}
	public boolean isObjective() {
		return objectiveTest;
	}
	
	public String toString() {
		return name+"="+result+" (expected "+expected+")";
	}
	public String getResult() {
		if (result !=null) {
			return result.toString();
		} else {
			return "(null)";
		}
	}
}
