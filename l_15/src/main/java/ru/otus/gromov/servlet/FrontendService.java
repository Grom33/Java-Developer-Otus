package ru.otus.gromov.servlet;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import ru.otus.gromov.messageSystem.Addressee;

public interface FrontendService extends Addressee {

	void addAnswer(long index, Object answer);

}
