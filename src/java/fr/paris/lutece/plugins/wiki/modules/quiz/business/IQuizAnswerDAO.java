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

import java.util.List;

import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * Data access object interface for quiz answers.
 */
public interface IQuizAnswerDAO
{
    /**
     * Inserts a new quiz answer into the database.
     *
     * @param answer
     *            The quiz answer to insert
     * @param plugin
     *            The plugin instance
     */
    void insert( QuizAnswer answer, Plugin plugin );

    /**
     * Deletes all answers associated with a specific question.
     *
     * @param nIdQuestion
     *            The question ID
     * @param plugin
     *            The plugin instance
     */
    void deleteByQuestion( int nIdQuestion, Plugin plugin );

    /**
     * Retrieves all answers for a specific question.
     *
     * @param nIdQuestion
     *            The question ID
     * @param plugin
     *            The plugin instance
     * @return A list of quiz answers for the question
     */
    List<QuizAnswer> selectByQuestion( int nIdQuestion, Plugin plugin );
}
