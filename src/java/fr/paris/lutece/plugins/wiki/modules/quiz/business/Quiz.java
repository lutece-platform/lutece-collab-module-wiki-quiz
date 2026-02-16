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
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Quiz entity representing a quiz within a book.
 */
public class Quiz implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int _nId;
    private int _nIdBook;

    @NotEmpty( message = "#i18n{module.wiki.quiz.validation.quiz.Title.notEmpty}" )
    @Size( max = 255, message = "#i18n{module.wiki.quiz.validation.quiz.Title.size}" )
    private String _strTitle;

    private String _strDescription;
    private Integer _nTimeLimitMinutes;
    private Integer _nMaxAttempts;
    private int _nPassingScore = 70;
    private boolean _bRandomQuestions;
    private boolean _bIsPublished;
    private int _nDisplayOrder;

    private transient List<QuizQuestion> _listQuestions;

    /**
     * Default constructor.
     */
    public Quiz( )
    {
    }

    /**
     * Gets the quiz ID.
     *
     * @return The quiz ID
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the quiz ID.
     *
     * @param nId
     *            The quiz ID
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Gets the associated book ID.
     *
     * @return The book ID
     */
    public int getIdBook( )
    {
        return _nIdBook;
    }

    /**
     * Sets the associated book ID.
     *
     * @param nIdBook
     *            The book ID
     */
    public void setIdBook( int nIdBook )
    {
        _nIdBook = nIdBook;
    }

    /**
     * Gets the quiz title.
     *
     * @return The quiz title
     */
    public String getTitle( )
    {
        return _strTitle;
    }

    /**
     * Sets the quiz title.
     *
     * @param strTitle
     *            The quiz title
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Gets the quiz description.
     *
     * @return The quiz description
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Sets the quiz description.
     *
     * @param strDescription
     *            The quiz description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Gets the time limit in minutes.
     *
     * @return The time limit in minutes, or null if no limit
     */
    public Integer getTimeLimitMinutes( )
    {
        return _nTimeLimitMinutes;
    }

    /**
     * Sets the time limit in minutes.
     *
     * @param nTimeLimitMinutes
     *            The time limit in minutes
     */
    public void setTimeLimitMinutes( Integer nTimeLimitMinutes )
    {
        _nTimeLimitMinutes = nTimeLimitMinutes;
    }

    /**
     * Gets the maximum number of attempts.
     *
     * @return The maximum attempts, or null if unlimited
     */
    public Integer getMaxAttempts( )
    {
        return _nMaxAttempts;
    }

    /**
     * Sets the maximum number of attempts.
     *
     * @param nMaxAttempts
     *            The maximum attempts
     */
    public void setMaxAttempts( Integer nMaxAttempts )
    {
        _nMaxAttempts = nMaxAttempts;
    }

    /**
     * Gets the passing score percentage.
     *
     * @return The passing score
     */
    public int getPassingScore( )
    {
        return _nPassingScore;
    }

    /**
     * Sets the passing score percentage.
     *
     * @param nPassingScore
     *            The passing score
     */
    public void setPassingScore( int nPassingScore )
    {
        _nPassingScore = nPassingScore;
    }

    /**
     * Gets whether questions should be randomized.
     *
     * @return True if questions should be randomized
     */
    public boolean getRandomQuestions( )
    {
        return _bRandomQuestions;
    }

    /**
     * Sets whether questions should be randomized.
     *
     * @param bRandomQuestions
     *            True if questions should be randomized
     */
    public void setRandomQuestions( boolean bRandomQuestions )
    {
        _bRandomQuestions = bRandomQuestions;
    }

    /**
     * Gets whether the quiz is published.
     *
     * @return True if the quiz is published
     */
    public boolean isPublished( )
    {
        return _bIsPublished;
    }

    /**
     * Sets whether the quiz is published.
     *
     * @param bIsPublished
     *            True if the quiz is published
     */
    public void setIsPublished( boolean bIsPublished )
    {
        _bIsPublished = bIsPublished;
    }

    /**
     * Gets the display order of the quiz.
     *
     * @return The display order
     */
    public int getDisplayOrder( )
    {
        return _nDisplayOrder;
    }

    /**
     * Sets the display order of the quiz.
     *
     * @param nDisplayOrder
     *            The display order
     */
    public void setDisplayOrder( int nDisplayOrder )
    {
        _nDisplayOrder = nDisplayOrder;
    }

    /**
     * Gets the list of quiz questions.
     *
     * @return The list of questions
     */
    public List<QuizQuestion> getQuestions( )
    {
        if ( _listQuestions == null )
        {
            _listQuestions = new ArrayList<>( );
        }
        return _listQuestions;
    }

    /**
     * Sets the list of quiz questions.
     *
     * @param listQuestions
     *            The list of questions
     */
    public void setQuestions( List<QuizQuestion> listQuestions )
    {
        _listQuestions = listQuestions;
    }

    /**
     * Checks if this quiz has a maximum attempt limit.
     *
     * @return True if a maximum attempt limit is set and greater than zero
     */
    public boolean hasMaxAttempts( )
    {
        return _nMaxAttempts != null && _nMaxAttempts > 0;
    }
}
