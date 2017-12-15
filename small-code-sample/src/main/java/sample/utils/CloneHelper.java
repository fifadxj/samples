package sample.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class CloneHelper<T> {
    public static void main(String[] args) throws Exception {
    	Department dep = new Department("dev", 20);
        Employee emp = new Employee("terry", 1000, dep);
        System.out.println(emp);

//        CloneHelper<Employee> cloneHelper = new CloneHelper<Employee>();
//        Employee newEmp = cloneHelper.cloneObjectBySerialization(emp);
//        System.out.println(newEmp);
        
        Employee newEmp = emp.clone();
        emp.department.size = 21;
        System.out.println(newEmp);

    }
    
    public T cloneObjectBySerialization(T original) {
		try {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bout);
			out.writeObject(original);
			
			ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
			ObjectInputStream in = new ObjectInputStream(bin);
			T cloned = (T) in.readObject();
			
			return cloned;
		} catch (Exception e) {
			throw new RuntimeException("clone object by serialization failed", e);
		}
	}

}

class Employee implements Serializable, Cloneable {
	public Employee clone() throws CloneNotSupportedException {
		//return super.clone();
		return new CloneHelper<Employee>().cloneObjectBySerialization(this);
	}
    public Employee(String name, double salary, Department department) {
        //System.out.println("new Employee()");
        this.name = name;
        this.salary = salary;
        this.department = department;
    }
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("name: " + name + "\n");
    	sb.append("salary: " + salary + "\n");
    	sb.append("department: (\n" + department + ")\n");
    	
        return sb.toString();
    }
    String name;
    double salary;
    Department department;
    
}

class Department implements Serializable {
    public Department(String name, int size) {
    	//System.out.println("new Department()");
    	this.name = name;
    	this.size = size;
    }
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("name: " + name + "\n");
    	sb.append("size: " + size + "\n");
    	
        return sb.toString();
    }
    String name;
    int size;
}