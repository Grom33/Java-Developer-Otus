package ru.otus.gromov.messageSystem;/*
 *   Created by Gromov Vitaly (Grom33), 2018
 *   e-mail: mr.gromov.vitaly@gmail.com
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Message {
	private final Address from;
	private final Address to;
	private final long index;

	public abstract void exec(Addressee addressee);

}
