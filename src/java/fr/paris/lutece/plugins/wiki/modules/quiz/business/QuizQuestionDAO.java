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
 * Implementation of quiz question data access object.
 */
@ApplicationScoped
public class QuizQuestionDAO implements IQuizQuestionDAO
{
    private static final String SQL_QUERY_SELECT = "SELECT id_question, id_quiz, question_type, question_text, explanation, points, display_order FROM wiki_quiz_question WHERE id_question = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO wiki_quiz_question ( id_quiz, question_type, question_text, explanation, points, display_order ) VALUES ( ?, ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE = "DELETE FROM wiki_quiz_question WHERE id_question = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE wiki_quiz_question SET id_quiz = ?, question_type = ?, question_text = ?, explanation = ?, points = ?, display_order = ? WHERE id_question = ?";
    private static final String SQL_QUERY_SELECT_BY_QUIZ = "SELECT id_question, id_quiz, question_type, question_text, explanation, points, display_order FROM wiki_quiz_question WHERE id_quiz = ? ORDER BY display_order";
    private static final String SQL_QUERY_MAX_DISPLAY_ORDER = "SELECT COALESCE(MAX(display_order), 0) FROM wiki_quiz_question WHERE id_quiz = ?";
    private static final String SQL_QUERY_SELECT_SOURCE_PAGES = "SELECT id_page FROM wiki_quiz_question_page WHERE id_question = ?";
    private static final String SQL_QUERY_INSERT_SOURCE_PAGE = "INSERT INTO wiki_quiz_question_page ( id_question, id_page ) VALUES ( ?, ? )";
    private static final String SQL_QUERY_DELETE_SOURCE_PAGES = "DELETE FROM wiki_quiz_question_page WHERE id_question = ?";

    private static final String COLUMN_ID_QUESTION = "id_question";
    private static final String COLUMN_ID_QUIZ = "id_quiz";
    private static final String COLUMN_QUESTION_TYPE = "question_type";
    private static final String COLUMN_QUESTION_TEXT = "question_text";
    private static final String COLUMN_EXPLANATION = "explanation";
    private static final String COLUMN_POINTS = "points";
    private static final String COLUMN_DISPLAY_ORDER = "display_order";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( QuizQuestion question, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, question.getIdQuiz( ) );
            daoUtil.setString( nIndex++, question.getQuestionType( ).getCode( ) );
            daoUtil.setString( nIndex++, question.getQuestionText( ) );
            daoUtil.setString( nIndex++, question.getExplanation( ) );
            daoUtil.setInt( nIndex++, question.getPoints( ) );
            daoUtil.setInt( nIndex++, question.getDisplayOrder( ) );

            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                question.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<QuizQuestion> load( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );

            QuizQuestion question = null;

            if ( daoUtil.next( ) )
            {
                question = dataToObject( daoUtil );
            }

            return Optional.ofNullable( question );
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
    public void store( QuizQuestion question, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, question.getIdQuiz( ) );
            daoUtil.setString( nIndex++, question.getQuestionType( ).getCode( ) );
            daoUtil.setString( nIndex++, question.getQuestionText( ) );
            daoUtil.setString( nIndex++, question.getExplanation( ) );
            daoUtil.setInt( nIndex++, question.getPoints( ) );
            daoUtil.setInt( nIndex++, question.getDisplayOrder( ) );

            daoUtil.setInt( nIndex, question.getId( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QuizQuestion> selectByQuiz( int nIdQuiz, Plugin plugin )
    {
        List<QuizQuestion> questionList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_QUIZ, plugin ) )
        {
            daoUtil.setInt( 1, nIdQuiz );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                questionList.add( dataToObject( daoUtil ) );
            }
        }

        return questionList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxDisplayOrder( int nIdQuiz, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_MAX_DISPLAY_ORDER, plugin ) )
        {
            daoUtil.setInt( 1, nIdQuiz );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                return daoUtil.getInt( 1 );
            }

            return 0;
        }
    }

    /**
     * Converts database row data to a QuizQuestion object.
     *
     * @param daoUtil
     *            The data access utility
     * @return The quiz question object
     */
    private QuizQuestion dataToObject( DAOUtil daoUtil )
    {
        QuizQuestion question = new QuizQuestion( );

        question.setId( daoUtil.getInt( COLUMN_ID_QUESTION ) );
        question.setIdQuiz( daoUtil.getInt( COLUMN_ID_QUIZ ) );
        question.setQuestionType( QuestionType.fromCode( daoUtil.getString( COLUMN_QUESTION_TYPE ) ) );
        question.setQuestionText( daoUtil.getString( COLUMN_QUESTION_TEXT ) );
        question.setExplanation( daoUtil.getString( COLUMN_EXPLANATION ) );
        question.setPoints( daoUtil.getInt( COLUMN_POINTS ) );
        question.setDisplayOrder( daoUtil.getInt( COLUMN_DISPLAY_ORDER ) );

        return question;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> selectSourcePageIds( int nIdQuestion, Plugin plugin )
    {
        List<Integer> listPageIds = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_SOURCE_PAGES, plugin ) )
        {
            daoUtil.setInt( 1, nIdQuestion );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                listPageIds.add( daoUtil.getInt( 1 ) );
            }
        }

        return listPageIds;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertSourcePages( int nIdQuestion, List<Integer> listPageIds, Plugin plugin )
    {
        for ( Integer nPageId : listPageIds )
        {
            try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_SOURCE_PAGE, plugin ) )
            {
                daoUtil.setInt( 1, nIdQuestion );
                daoUtil.setInt( 2, nPageId );
                daoUtil.executeUpdate( );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteSourcePages( int nIdQuestion, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_SOURCE_PAGES, plugin ) )
        {
            daoUtil.setInt( 1, nIdQuestion );
            daoUtil.executeUpdate( );
        }
    }
}
