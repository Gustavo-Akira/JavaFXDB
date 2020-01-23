package model.services;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;
public class DepartmentService {
	private DepartmentDao d = DaoFactory.createDepartmentDao();
	public List<Department> findAll(){
		return d.findAll();
	}
	public void SorUp(Department o) {
		if(o.getId()==null) {
			d.insert(o);
		}else {
			d.update(o);
		}
	}
	public void remove(Department o) {
		d.deleteById(o.getId());
	}
}
