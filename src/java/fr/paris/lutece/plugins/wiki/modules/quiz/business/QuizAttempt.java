/*
 * Copyright (c) 2002-2026, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.wiki.modules.quiz.business;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Quiz attempt entity representing a user's attempt to take a quiz.
 */
public class QuizAttempt implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int _nId;
    private int _nIdQuiz;
    private String _strUserGuid;
    private Integer _nScore;
    private Boolean _bPassed;
    private Timestamp _dateStartedAt;
    private Timestamp _dateCompletedAt;

    private transient List<QuizResponse> _listResponses;

    /**
     * Default constructor.
     */
    public QuizAttempt( )
    {
    }

    /**
     * Gets the attempt ID.
     *
     * @return The attempt ID
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the attempt ID.
     *
     * @param nId
     *            The attempt ID
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Gets the associated quiz ID.
     *
     * @return The quiz ID
     */
    public int getIdQuiz( )
    {
        return _nIdQuiz;
    }

    /**
     * Sets the associated quiz ID.
     *
     * @param nIdQuiz
     *            The quiz ID
     */
    public void setIdQuiz( int nIdQuiz )
    {
        _nIdQuiz = nIdQuiz;
    }

    /**
     * Gets the user GUID.
     *
     * @return The user GUID
     */
    public String getUserGuid( )
    {
        return _strUserGuid;
    }

    /**
     * Sets the user GUID.
     *
     * @param strUserGuid
     *            The user GUID
     */
    public void setUserGuid( String strUserGuid )
    {
        _strUserGuid = strUserGuid;
    }

    /**
     * Gets the score achieved.
     *
     * @return The score, or null if not yet scored
     */
    public Integer getScore( )
    {
        return _nScore;
    }

    /**
     * Sets the score achieved.
     *
     * @param nScore
     *            The score
     */
    public void setScore( Integer nScore )
    {
        _nScore = nScore;
    }

    /**
     * Gets whether the attempt passed.
     *
     * @return True if passed, false if failed, null if not yet evaluated
     */
    public Boolean getPassed( )
    {
        return _bPassed;
    }

    /**
     * Sets whether the attempt passed.
     *
     * @param bPassed
     *            True if passed, false otherwise
     */
    public void setPassed( Boolean bPassed )
    {
        _bPassed = bPassed;
    }

    /**
     * Gets the timestamp when the attempt started.
     *
     * @return The start timestamp
     */
    public Timestamp getStartedAt( )
    {
        return _dateStartedAt;
    }

    /**
     * Sets the timestamp when the attempt started.
     *
     * @param dateStartedAt
     *            The start timestamp
     */
    public void setStartedAt( Timestamp dateStartedAt )
    {
        _dateStartedAt = dateStartedAt;
    }

    /**
     * Gets the timestamp when the attempt completed.
     *
     * @return The completion timestamp, or null if not yet completed
     */
    public Timestamp getCompletedAt( )
    {
        return _dateCompletedAt;
    }

    /**
     * Sets the timestamp when the attempt completed.
     *
     * @param dateCompletedAt
     *            The completion timestamp
     */
    public void setCompletedAt( Timestamp dateCompletedAt )
    {
        _dateCompletedAt = dateCompletedAt;
    }

    /**
     * Checks if this attempt has been completed.
     *
     * @return True if completed, false otherwise
     */
    public boolean isCompleted( )
    {
        return _dateCompletedAt != null;
    }

    /**
     * Gets the list of responses for this attempt.
     *
     * @return The list of responses
     */
    public List<QuizResponse> getResponses( )
    {
        if ( _listResponses == null )
        {
            _listResponses = new ArrayList<>( );
        }
        return _listResponses;
    }

    /**
     * Sets the list of responses for this attempt.
     *
     * @param listResponses
     *            The list of responses
     */
    public void setResponses( List<QuizResponse> listResponses )
    {
        _listResponses = listResponses;
    }
}
