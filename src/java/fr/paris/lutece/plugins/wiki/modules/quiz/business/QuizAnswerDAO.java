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

/**
 * Implementation of quiz answer data access object.
 */
@ApplicationScoped
public class QuizAnswerDAO implements IQuizAnswerDAO
{
    private static final String SQL_QUERY_INSERT = "INSERT INTO wiki_quiz_answer ( id_question, answer_text, is_correct, match_target, correct_order, display_order ) VALUES ( ?, ?, ?, ?, ?, ? )";
    private static final String SQL_QUERY_DELETE_BY_QUESTION = "DELETE FROM wiki_quiz_answer WHERE id_question = ?";
    private static final String SQL_QUERY_SELECT_BY_QUESTION = "SELECT id_answer, id_question, answer_text, is_correct, match_target, correct_order, display_order FROM wiki_quiz_answer WHERE id_question = ? ORDER BY display_order";

    private static final String COLUMN_ID_ANSWER = "id_answer";
    private static final String COLUMN_ID_QUESTION = "id_question";
    private static final String COLUMN_ANSWER_TEXT = "answer_text";
    private static final String COLUMN_IS_CORRECT = "is_correct";
    private static final String COLUMN_MATCH_TARGET = "match_target";
    private static final String COLUMN_CORRECT_ORDER = "correct_order";
    private static final String COLUMN_DISPLAY_ORDER = "display_order";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( QuizAnswer answer, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setInt( nIndex++, answer.getIdQuestion( ) );
            daoUtil.setString( nIndex++, answer.getAnswerText( ) );
            daoUtil.setBoolean( nIndex++, answer.getIsCorrect( ) );
            daoUtil.setString( nIndex++, answer.getMatchTarget( ) );

            if ( answer.getCorrectOrder( ) != null )
            {
                daoUtil.setInt( nIndex++, answer.getCorrectOrder( ) );
            }
            else
            {
                daoUtil.setIntNull( nIndex++ );
            }

            daoUtil.setInt( nIndex++, answer.getDisplayOrder( ) );

            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                answer.setId( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByQuestion( int nIdQuestion, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_QUESTION, plugin ) )
        {
            daoUtil.setInt( 1, nIdQuestion );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<QuizAnswer> selectByQuestion( int nIdQuestion, Plugin plugin )
    {
        List<QuizAnswer> answerList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_QUESTION, plugin ) )
        {
            daoUtil.setInt( 1, nIdQuestion );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                answerList.add( dataToObject( daoUtil ) );
            }
        }

        return answerList;
    }

    /**
     * Converts database row data to a QuizAnswer object.
     *
     * @param daoUtil
     *            The data access utility
     * @return The quiz answer object
     */
    private QuizAnswer dataToObject( DAOUtil daoUtil )
    {
        QuizAnswer answer = new QuizAnswer( );

        answer.setId( daoUtil.getInt( COLUMN_ID_ANSWER ) );
        answer.setIdQuestion( daoUtil.getInt( COLUMN_ID_QUESTION ) );
        answer.setAnswerText( daoUtil.getString( COLUMN_ANSWER_TEXT ) );
        answer.setIsCorrect( daoUtil.getBoolean( COLUMN_IS_CORRECT ) );
        answer.setMatchTarget( daoUtil.getString( COLUMN_MATCH_TARGET ) );
        answer.setCorrectOrder( daoUtil.getObject( COLUMN_CORRECT_ORDER, Integer.class ) );
        answer.setDisplayOrder( daoUtil.getInt( COLUMN_DISPLAY_ORDER ) );

        return answer;
    }
}
