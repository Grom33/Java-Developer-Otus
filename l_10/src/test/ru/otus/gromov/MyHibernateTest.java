package ru.otus.gromov;

import org.junit.Test;
import ru.otus.gromov.domain.UserDataSet;
import ru.otus.gromov.sql.ConnectionHelper;
import ru.otus.gromov.sql.SQLHelper;

public class MyHibernateTest {

	@Test
	public void save(){
		MyHibernate myHibernate = new MyHibernate();
		//myHibernate.saveObject(new UserDataSet(1l, "dsafd", 12, true));

		myHibernate.save(new UserDataSet(1l, "dsafd", 12, true));
        myHibernate.save(new UserDataSet(2l, "rtytttdsafd", 12, true));
        System.out.println(myHibernate.loadObject(1, UserDataSet.class));
		//myHibernate.execute("SELECT * FROM USERDATASET");
	}
}