package net.iung.simplySnow;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class Main implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
//	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static final Logger LOGGER = new NoLog();


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Rules.register();


		//LOGGER.info("Hello Fabric world!");
	}
}

class NoLog implements Logger{

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean isTraceEnabled() {
		return false;
	}

	@Override
	public void trace(String s) {

	}

	@Override
	public void trace(String s, Object o) {

	}

	@Override
	public void trace(String s, Object o, Object o1) {

	}

	@Override
	public void trace(String s, Object... objects) {

	}

	@Override
	public void trace(String s, Throwable throwable) {

	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		return false;
	}

	@Override
	public void trace(Marker marker, String s) {

	}

	@Override
	public void trace(Marker marker, String s, Object o) {

	}

	@Override
	public void trace(Marker marker, String s, Object o, Object o1) {

	}

	@Override
	public void trace(Marker marker, String s, Object... objects) {

	}

	@Override
	public void trace(Marker marker, String s, Throwable throwable) {

	}

	@Override
	public boolean isDebugEnabled() {
		return false;
	}

	@Override
	public void debug(String s) {

	}

	@Override
	public void debug(String s, Object o) {

	}

	@Override
	public void debug(String s, Object o, Object o1) {

	}

	@Override
	public void debug(String s, Object... objects) {

	}

	@Override
	public void debug(String s, Throwable throwable) {

	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return false;
	}

	@Override
	public void debug(Marker marker, String s) {

	}

	@Override
	public void debug(Marker marker, String s, Object o) {

	}

	@Override
	public void debug(Marker marker, String s, Object o, Object o1) {

	}

	@Override
	public void debug(Marker marker, String s, Object... objects) {

	}

	@Override
	public void debug(Marker marker, String s, Throwable throwable) {

	}

	@Override
	public boolean isInfoEnabled() {
		return false;
	}

	@Override
	public void info(String s) {

	}

	@Override
	public void info(String s, Object o) {

	}

	@Override
	public void info(String s, Object o, Object o1) {

	}

	@Override
	public void info(String s, Object... objects) {

	}

	@Override
	public void info(String s, Throwable throwable) {

	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return false;
	}

	@Override
	public void info(Marker marker, String s) {

	}

	@Override
	public void info(Marker marker, String s, Object o) {

	}

	@Override
	public void info(Marker marker, String s, Object o, Object o1) {

	}

	@Override
	public void info(Marker marker, String s, Object... objects) {

	}

	@Override
	public void info(Marker marker, String s, Throwable throwable) {

	}

	@Override
	public boolean isWarnEnabled() {
		return false;
	}

	@Override
	public void warn(String s) {

	}

	@Override
	public void warn(String s, Object o) {

	}

	@Override
	public void warn(String s, Object... objects) {

	}

	@Override
	public void warn(String s, Object o, Object o1) {

	}

	@Override
	public void warn(String s, Throwable throwable) {

	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return false;
	}

	@Override
	public void warn(Marker marker, String s) {

	}

	@Override
	public void warn(Marker marker, String s, Object o) {

	}

	@Override
	public void warn(Marker marker, String s, Object o, Object o1) {

	}

	@Override
	public void warn(Marker marker, String s, Object... objects) {

	}

	@Override
	public void warn(Marker marker, String s, Throwable throwable) {

	}

	@Override
	public boolean isErrorEnabled() {
		return false;
	}

	@Override
	public void error(String s) {

	}

	@Override
	public void error(String s, Object o) {

	}

	@Override
	public void error(String s, Object o, Object o1) {

	}

	@Override
	public void error(String s, Object... objects) {

	}

	@Override
	public void error(String s, Throwable throwable) {

	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return false;
	}

	@Override
	public void error(Marker marker, String s) {

	}

	@Override
	public void error(Marker marker, String s, Object o) {

	}

	@Override
	public void error(Marker marker, String s, Object o, Object o1) {

	}

	@Override
	public void error(Marker marker, String s, Object... objects) {

	}

	@Override
	public void error(Marker marker, String s, Throwable throwable) {

	}
}