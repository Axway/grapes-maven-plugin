package org.axway.grapes.maven;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

public class TestableLog implements Log
{
    private final List<String> errors = new ArrayList<String>();
    private final List<String> infos = new ArrayList<String>();
    private final List<String> warns = new ArrayList<String>();

    public List<String> getErrors()
    {
        return errors;
    }

    public List<String> getInfos()
    {
        return infos;
    }

    public List<String> getWarns()
    {
        return warns;
    }

    @Override
    public boolean isDebugEnabled()
    {
        return false;
    }

    @Override
    public void debug(final CharSequence content)
    {

    }

    @Override
    public void debug(final CharSequence content, final Throwable error)
    {

    }

    @Override
    public void debug(final Throwable error)
    {

    }

    @Override
    public boolean isInfoEnabled()
    {
        return true;
    }

    @Override
    public void info(final CharSequence content)
    {
        infos.add(content.toString());
    }

    @Override
    public void info(final CharSequence content, final Throwable error)
    {
        infos.add(content.toString());
    }

    @Override
    public void info(final Throwable error)
    {

    }

    @Override
    public boolean isWarnEnabled()
    {
        return true;
    }

    @Override
    public void warn(final CharSequence content)
    {
        warns.add(content.toString());
    }

    @Override
    public void warn(final CharSequence content, final Throwable error)
    {
        warns.add(content.toString());
    }

    @Override
    public void warn(final Throwable error)
    {

    }

    @Override
    public boolean isErrorEnabled()
    {
        return true;
    }

    @Override
    public void error(final CharSequence content)
    {
        errors.add(content.toString());
    }

    @Override
    public void error(final CharSequence content, final Throwable error)
    {
        errors.add(content.toString());
    }

    @Override
    public void error(final Throwable error)
    {

    }
}
