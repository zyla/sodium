using System;

namespace Sodium
{
    internal static class CoalesceHandler<T>
    {
        internal static Action<Transaction, T> Create(Func<T, T, T> f, Stream<T> @out)
        {
            bool accumValid = false;
            T accum = default(T);

            return (trans1, a) =>
            {
                if (accumValid)
                {
                    accum = f(accum, a);
                }
                else
                {
                    trans1.Prioritized(@out.Node, trans2 =>
                    {
                        // ReSharper disable once AccessToModifiedClosure
                        @out.Send(trans2, accum);
                        accumValid = false;
                        accum = default(T);
                    });
                    accum = a;
                    accumValid = true;
                }
            };
        }
    }
}