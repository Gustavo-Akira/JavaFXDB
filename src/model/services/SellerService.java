package model.services;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;
public class SellerService {
		private SellerDao d = DaoFactory.createSellerDao();
		public List<Seller> findAll(){
			return d.findAll();
		}
		public void SorUp(Seller o) {
			if(o.getId()==null) {
				d.insert(o);
			}else {
				d.update(o);
			}
		}
		public void remove(Seller o) {
			d.deleteById(o.getId());
		}
	}


