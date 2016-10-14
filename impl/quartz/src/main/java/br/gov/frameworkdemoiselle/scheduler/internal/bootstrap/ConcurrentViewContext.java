package br.gov.frameworkdemoiselle.scheduler.internal.bootstrap;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Alternative;

import br.gov.frameworkdemoiselle.annotation.Priority;
import br.gov.frameworkdemoiselle.context.ViewContext;
import br.gov.frameworkdemoiselle.internal.context.TemporaryViewContextImpl;
import br.gov.frameworkdemoiselle.scheduler.internal.bootstrap.Concurrent;

@Priority(Priority.L2_PRIORITY)
@Alternative
@Concurrent
public class ConcurrentViewContext implements ViewContext {
	
	private final ThreadLocal<ViewContext> threadLocalContext = new ThreadLocal<ViewContext>();

	public ConcurrentViewContext() {
		System.out.println("ConcurrentViewContext Created");
	}
	
	private ViewContext getContext() {
		ViewContext context = threadLocalContext.get(); 
		if (context == null) {
			context = new TemporaryViewContextImpl();
			threadLocalContext.set(context);
		}
		return context;
	}
	
	@Override
	public boolean activate() {
		return getContext().activate();
	}

	@Override
	public void deactivate() {
		getContext().deactivate();
	}

	@Override
	public Class<? extends Annotation> getScope() {
		return getContext().getScope();
	}

	@Override
	public <T> T get(Contextual<T> contextual,
			CreationalContext<T> creationalContext) {
		return getContext().get(contextual, creationalContext);
	}

	@Override
	public <T> T get(Contextual<T> contextual) {
		return getContext().get(contextual);
	}

	@Override
	public boolean isActive() {
		return getContext().isActive();
	}
}
