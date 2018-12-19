package ru.gromov.l162.app;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by tully.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class Msg {
}
