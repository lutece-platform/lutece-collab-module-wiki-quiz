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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import jakarta.enterprise.context.ApplicationScoped;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of quiz data access object.
 */
@ApplicationScoped
public class QuizDAO implements IQuizDAO
{
    private static final String SQL_QUERY_SELECT = "SELECT id_quiz, id_book, title, description, time_limit_minutes, max_attempts, passing_score, random_questions, is_published, display_order FROM wiki_quiz WHERE id_quiz = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO wiki_quiz ( id_book, title, description, time_limit_minutes, max_attempts, passing_score, random_questions, is_published, display_order ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = "DELETE FROM wiki_quiz WHERE id_quiz = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE wiki_quiz SET id_book = ?, title = ?, description = ?, time_limit_minutes = ?, max_attempts = ?, passing_score = ?, random_questions = ?, is_published = ?, display_order = ? WHERE id_quiz = ?";
    private static final String SQL_QUERY_SELECT_BY_BOOK = "SELECT id_quiz, id_book, title, description, time_limit_minutes, max_attempts, passing_score, random_questions, is_published, display_order FROM wiki_quiz WHERE id_book = ? ORDER BY display_order";
    private static final String SQL_QUERY_SELECT_PUBLISHED_BY_BOOK = "SELECT id_quiz, id_book, title, description, time_limit_minutes, max_attempts, passing_score, random_questions, is_published, display_order FROM wiki_quiz WHERE id_book = ? AND is_published = 1 ORDER BY display_order";

    private static final String COLUMN_ID_QUIZ = "id_quiz";
    private static final String COLUMN_ID_BOOK = "id_book";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_TIME_LIMIT_MINUTES = "time_limit_minutes";
    private static final String COLUMN_MAX_ATTEMPTS = "max_attempts";
    private static final String COLUMN_PASSING_SCORE = "passing_score";
    private static final String COLUMN_RANDOM_QUESTIONS = "random_questions";
    private static final String COLUMN_IS_PUBLISHED = "is_published";
    private static final String COLUMN_DISPLAY_ORDER = "display_order";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( Quiz quiz, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, quiz.getIdBook( ) );
            daoUtil.setString( nIndex++, quiz.getTitle( ) );
            daoUtil.setString( nIndex++, quiz.getDescription( ) );

            if ( quiz.getTimeLimitMinutes( ) != null )
            {
                daoUtil.setInt( nIndex++, quiz.getTimeLimitMinutes( ) );
            }
            else
            {
                daoUtil.setIntNull( nIndex++ );
            }

            if ( quiz.getMaxAttempts( ) != null )
            {
                daoUtil.setInt( nIndex++, quiz.getMaxAttempts( ) );
            }
            else
            {
                daoUtil.setIntNull( nIndex++ );
            }

            daoUtil.setInt( nIndex++, quiz.getPassingScore( ) );
            daoUtil.setBoolean( nIndex++, quiz.getRandomQuestions( ) );
            daoUtil.setBoolean( nIndex++, quiz.isPublished( ) );
            daoUtil.setInt( nIndex++, quiz.getDisplayOrder( ) );

            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                quiz.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Quiz> load( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );

            Quiz quiz = null;

            if ( daoUtil.next( ) )
            {
                quiz = dataToObject( daoUtil );
            }

            return Optional.ofNullable( quiz );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( Quiz quiz, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, quiz.getIdBook( ) );
            daoUtil.setString( nIndex++, quiz.getTitle( ) );
            daoUtil.setString( nIndex++, quiz.getDescription( ) );

            if ( quiz.getTimeLimitMinutes( ) != null )
            {
                daoUtil.setInt( nIndex++, quiz.getTimeLimitMinutes( ) );
            }
            else
            {
                daoUtil.setIntNull( nIndex++ );
            }

            if ( quiz.getMaxAttempts( ) != null )
            {
                daoUtil.setInt( nIndex++, quiz.getMaxAttempts( ) );
            }
            else
            {
                daoUtil.setIntNull( nIndex++ );
            }

            daoUtil.setInt( nIndex++, quiz.getPassingScore( ) );
            daoUtil.setBoolean( nIndex++, quiz.getRandomQuestions( ) );
            daoUtil.setBoolean( nIndex++, quiz.isPublished( ) );
            daoUtil.setInt( nIndex++, quiz.getDisplayOrder( ) );

            daoUtil.setInt( nIndex, quiz.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Quiz> selectByBook( int nIdBook, Plugin plugin )
    {
        List<Quiz> quizList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_BOOK, plugin ) )
        {
            daoUtil.setInt( 1, nIdBook );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                quizList.add( dataToObject( daoUtil ) );
            }
        }

        return quizList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Quiz> selectPublishedByBook( int nIdBook, Plugin plugin )
    {
        List<Quiz> quizList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PUBLISHED_BY_BOOK, plugin ) )
        {
            daoUtil.setInt( 1, nIdBook );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                quizList.add( dataToObject( daoUtil ) );
            }
        }

        return quizList;
    }

    /**
     * Converts database row data to a Quiz object.
     *
     * @param daoUtil
     *            The data access utility
     * @return The quiz object
     */
    private Quiz dataToObject( DAOUtil daoUtil )
    {
        Quiz quiz = new Quiz( );

        quiz.setId( daoUtil.getInt( COLUMN_ID_QUIZ ) );
        quiz.setIdBook( daoUtil.getInt( COLUMN_ID_BOOK ) );
        quiz.setTitle( daoUtil.getString( COLUMN_TITLE ) );
        quiz.setDescription( daoUtil.getString( COLUMN_DESCRIPTION ) );
        quiz.setTimeLimitMinutes( daoUtil.getObject( COLUMN_TIME_LIMIT_MINUTES, Integer.class ) );
        quiz.setMaxAttempts( daoUtil.getObject( COLUMN_MAX_ATTEMPTS, Integer.class ) );
        quiz.setPassingScore( daoUtil.getInt( COLUMN_PASSING_SCORE ) );
        quiz.setRandomQuestions( daoUtil.getBoolean( COLUMN_RANDOM_QUESTIONS ) );
        quiz.setIsPublished( daoUtil.getBoolean( COLUMN_IS_PUBLISHED ) );
        quiz.setDisplayOrder( daoUtil.getInt( COLUMN_DISPLAY_ORDER ) );

        return quiz;
    }
}
